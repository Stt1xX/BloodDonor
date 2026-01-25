package com.bloodlink.service;

import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.DTOs.BloodRequestDTOfrom;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.RequestToBank;
import com.bloodlink.entities.User;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.RequestStatus;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.entities.specifications.RequestToBankSpecs;
import com.bloodlink.exceptions.AccessDeniedException;
import com.bloodlink.exceptions.BusinessException;
import com.bloodlink.exceptions.ResourceNotFoundException;
import com.bloodlink.repositories.BloodRequestRepository;
import com.bloodlink.repositories.OrganizationRepository;
import com.bloodlink.repositories.RequestToBankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static com.bloodlink.entities.specifications.RequestToBankSpecs.withAnyStatus;
import static com.bloodlink.entities.specifications.RequestToBankSpecs.withMedicalInstitution;

@RequiredArgsConstructor
@Service
@Slf4j
public class BloodRequestsService {

    private final UserService userService;
    private final OrganizationRepository organizationRepository;
    private final RequestToBankRepository requestToBankRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final BloodUnitsService bloodUnitsService;
    private final NotificationService notificationService;

    public Page<RequestToBank> getRequestsForBanker(BloodGroup group, RhFactor rhesus, Boolean reverse,
                                                    Pageable page) {
        User caller = userService.getCurrentUser();
        Organization org = caller.getOrganization();
        
        Specification<RequestToBank> filters = RequestToBankSpecs.withFiltersAndBank(
                org, group, rhesus, 
                List.of(RequestStatus.PENDING, RequestStatus.COMPLETED, RequestStatus.REJECTED));
        
        Sort sort = reverse != null && reverse ? page.getSort().descending() : page.getSort();
        return requestToBankRepository.findAll(filters, PageRequest.of(page.getPageNumber(),
                page.getPageSize(), sort));
    }

    public Page<RequestToBank> getRequestsForMed(RequestStatus status, Pageable page) {
        User caller = userService.getCurrentUser();
        Organization org = caller.getOrganization();
        
        Specification<RequestToBank> filters = Specification
                .where(withMedicalInstitution(org))
                .and(withAnyStatus(status == null 
                        ? List.of(RequestStatus.PENDING, RequestStatus.COMPLETED, RequestStatus.REJECTED) 
                        : List.of(status)));

        return requestToBankRepository.findAll(filters, PageRequest.of(page.getPageNumber(),
                page.getPageSize()));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Retryable(include = {SQLException.class})
    public BloodRequest save(BloodRequestDTOfrom dto) {
        User caller = userService.getCurrentUser();
        log.info("Creating blood request by user={}", caller.getEmail());
        
        BloodRequest request = dto.convert(caller);
        request = bloodRequestRepository.save(request);

        for (Long bankId : dto.getBloodBanks()) {
            Organization bank = findAndValidateBank(bankId);
            createRequestToBank(request, bank);
            notificationService.createNewRequestNotification(bank, request);
        }
        
        log.info("Blood request created id={}", request.getId());
        return request;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Retryable(include = {SQLException.class})
    public RequestToBank accept(Long id) {
        User caller = userService.getCurrentUser();
        log.info("Accepting request id={} by user={}", id, caller.getEmail());
        
        RequestToBank reqToBank = findAndValidateRequestToBank(id);
        validateBankPermission(reqToBank, caller);
        validatePendingStatus(reqToBank);

        BloodRequest request = reqToBank.getRequest();
        bloodUnitsService.fillBloodRequest(request.getBloodGroup(), request.getRhFactor(), request.getVolumeNeeded());

        updateRequestStatus(reqToBank, RequestStatus.COMPLETED, caller);
        cancelOtherPendingRequests(reqToBank);
        notificationService.createChangeStatusNotification(reqToBank);
        
        log.info("Request id={} accepted successfully", id);
        return reqToBank;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Retryable(include = {SQLException.class})
    public RequestToBank reject(Long id, String reason) {
        User caller = userService.getCurrentUser();
        log.info("Rejecting request id={} by user={}, reason={}", id, caller.getEmail(), reason);
        
        RequestToBank reqToBank = findAndValidateRequestToBank(id);
        validateBankPermission(reqToBank, caller);
        validatePendingStatus(reqToBank);

        reqToBank.setStatus(RequestStatus.REJECTED);
        reqToBank.setRejectionReason(reason);
        reqToBank.setBanker(caller);
        notificationService.createChangeStatusNotification(reqToBank);
        
        log.info("Request id={} rejected successfully", id);
        return reqToBank;
    }

    // ==================== Private Helper Methods ====================

    private Organization findAndValidateBank(Long bankId) {
        Organization bank = organizationRepository.findById(bankId)
                .orElseThrow(() -> new ResourceNotFoundException("Организация не найдена: " + bankId));
        
        if (bank.getType() != OrganizationType.BLOOD_BANK) {
            throw new BusinessException("Указанная организация не является банком крови");
        }
        return bank;
    }

    private RequestToBank findAndValidateRequestToBank(Long id) {
        return requestToBankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запрос не найден: " + id));
    }

    private void validateBankPermission(RequestToBank reqToBank, User caller) {
        if (!Objects.equals(reqToBank.getBloodBank().getId(), caller.getOrganization().getId())) {
            throw new AccessDeniedException("Это не ваш запрос");
        }
    }

    private void validatePendingStatus(RequestToBank reqToBank) {
        if (reqToBank.getStatus() != RequestStatus.PENDING) {
            throw new BusinessException("Этот запрос не ожидает обработки. " +
                    "Возможно, он уже принят, отклонён или удалён.");
        }
    }

    private void createRequestToBank(BloodRequest request, Organization bank) {
        RequestToBank bankRequest = new RequestToBank();
        bankRequest.setBloodBank(bank);
        bankRequest.setRequest(request);
        requestToBankRepository.save(bankRequest);
    }

    private void updateRequestStatus(RequestToBank reqToBank, RequestStatus status, User banker) {
        reqToBank.setStatus(status);
        reqToBank.setBanker(banker);
        requestToBankRepository.save(reqToBank);
    }

    private void cancelOtherPendingRequests(RequestToBank completedRequest) {
        for (RequestToBank req : completedRequest.getRequest().getBankRequests()) {
            if (req.getStatus() == RequestStatus.PENDING) {
                req.setStatus(RequestStatus.DELETED);
            }
        }
    }
}
