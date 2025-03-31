package com.bloodlink.service;

import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.DTOs.BloodRequestDTOfrom;
import com.bloodlink.entities.RequestToBank;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.RequestStatus;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.entities.specifications.RequestToBankSpecs;
import com.bloodlink.repositories.BloodRequestRepository;
import com.bloodlink.repositories.OrganizationRepository;
import com.bloodlink.repositories.RequestToBankRepository;
import lombok.RequiredArgsConstructor;
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
import java.util.logging.Logger;

import static com.bloodlink.entities.specifications.RequestToBankSpecs.withAnyStatus;
import static com.bloodlink.entities.specifications.RequestToBankSpecs.withMedicalInstitution;

@RequiredArgsConstructor
@Service
public class BloodRequestsService {

    private final UserService userService;
    private final OrganizationRepository organizationRepository;
    private final RequestToBankRepository requestToBankRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final BloodUnitsService bloodUnitsService;
    private final NotificationService notificationService;

    private final Logger logger = Logger.getLogger(BloodRequestsService.class.getName());
    public Page<RequestToBank> getRequestsForBanker(BloodGroup group, RhFactor rhesus, Boolean reverse,
                                                    Pageable page) {

        var caller = userService.getCurrentUser();
        if (caller.isEmpty()) {
            return Page.empty();
        }
        var org = caller.get().getOrganization();
        Specification<RequestToBank> filters = RequestToBankSpecs.withFiltersAndBank(org, group, rhesus, List.of(RequestStatus.PENDING
                , RequestStatus.COMPLETED,
                RequestStatus.REJECTED));
        Sort sort = reverse != null && reverse ? page.getSort().descending() : page.getSort();
        return requestToBankRepository.findAll(filters, PageRequest.of(page.getPageNumber(),
                page.getPageSize(), sort));
    }

    public Page<RequestToBank> getRequestsForMed(RequestStatus status,
                                                 Pageable page) {

        var caller = userService.getCurrentUser();
        if (caller.isEmpty()) {
            return Page.empty();
        }
        var org = caller.get().getOrganization();
        Specification<RequestToBank> filters = Specification
                .where(withMedicalInstitution(org))
                .and(withAnyStatus(status == null ? List.of(RequestStatus.PENDING, RequestStatus.COMPLETED,
                        RequestStatus.REJECTED) : List.of(status)));

        return requestToBankRepository.findAll(filters, PageRequest.of(page.getPageNumber(),
                page.getPageSize()));
    }


    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = IllegalArgumentException.class)
    @Retryable(include = {SQLException.class})
    public String save(BloodRequestDTOfrom dto) {
        var caller = userService.getCurrentUser();
        if (caller.isEmpty()) {
            return "Не могу распознать вас";
        }
        BloodRequest request = dto.convert(caller.get());
        request = bloodRequestRepository.save(request);

        for (var bankId : dto.getBloodBanks()) {
            var bankOpt = organizationRepository.findById(bankId);
            if (bankOpt.isEmpty()) {
                throw new IllegalArgumentException("Указанная организация не существует");
            }
            if (bankOpt.get().getType() != OrganizationType.BLOOD_BANK) {
                throw new IllegalArgumentException("Указанная организация не является банком");
            }
            var bankRequest = new RequestToBank();
            bankRequest.setBloodBank(bankOpt.get());
            bankRequest.setRequest(request);
            requestToBankRepository.save(bankRequest);

            logger.info( bankOpt.get().getMembers().toString());
            //Sending Notifications
            notificationService.createNewRequestNotification(bankOpt.get(), request);
        }
        return "Запрос на кровь успешно добавлен!";
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = IllegalArgumentException.class)
    @Retryable(include = {SQLException.class})
    public String accept(Long id) {
        var callerOpt = userService.getCurrentUser();
        if (callerOpt.isEmpty()) {
            return "Не могу распознать вас";
        }
        var reqToBankOpt = requestToBankRepository.findById(id);
        if (reqToBankOpt.isEmpty()) {
            throw new IllegalArgumentException("Предоставлен невалидный id запроса");
        }
        var reqToBank = reqToBankOpt.get();
        var caller = callerOpt.get();
        if (!Objects.equals(reqToBank.getBloodBank().getId(), caller.getOrganization().getId())) {
            throw new IllegalArgumentException("Это не ваш запрос");
        }
        if (reqToBank.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Этот запрос не ожидает принятия. Возможно, он уже принят, отклонён " +
                    "или удалён.");
        }

        var request = reqToBank.getRequest();
        bloodUnitsService.fillBloodRequest(request.getBloodGroup(), request.getRhFactor(), request.getVolumeNeeded());

        reqToBank.setStatus(RequestStatus.COMPLETED);
        reqToBank.setBanker(caller);
        for (var req : reqToBank.getRequest().getBankRequests()) {
            if (req.getStatus() != RequestStatus.COMPLETED) {
                req.setStatus(RequestStatus.REJECTED);
            }
        }

        return "Заявка успешно принята";
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = IllegalArgumentException.class)
    @Retryable(include = {SQLException.class})
    public String reject(Long id, String reason) {
        var callerOpt = userService.getCurrentUser();
        if (callerOpt.isEmpty()) {
            return "Не могу распознать вас";
        }
        var reqToBankOpt = requestToBankRepository.findById(id);
        if (reqToBankOpt.isEmpty()) {
            throw new IllegalArgumentException("Предоставлен невалидный id запроса");
        }
        var reqToBank = reqToBankOpt.get();
        var caller = callerOpt.get();
        if (!Objects.equals(reqToBank.getBloodBank().getId(), caller.getOrganization().getId())) {
            throw new IllegalArgumentException("Это не ваш запрос");
        }

        if (reqToBank.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Этот запрос не ожидает принятия. Возможно, он уже принят, отклонён " +
                    "или удалён.");
        }

        reqToBank.setStatus(RequestStatus.REJECTED);
        reqToBank.setRejectionReason(reason);
        reqToBank.setBanker(caller);
        return "Заявка успешно отклонена";
    }

}
