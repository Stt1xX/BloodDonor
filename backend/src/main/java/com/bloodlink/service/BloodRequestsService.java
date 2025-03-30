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

import static com.bloodlink.entities.specifications.RequestToBankSpecs.withAnyStatus;
import static com.bloodlink.entities.specifications.RequestToBankSpecs.withMedicalInstitution;

@RequiredArgsConstructor
@Service
public class BloodRequestsService {

    private final UserService userService;
    private final OrganizationRepository organizationRepository;
    private final RequestToBankRepository requestToBankRepository;
    private final BloodRequestRepository bloodRequestRepository;

    public Page<RequestToBank> getRequestsForBanker(BloodGroup group, RhFactor rhesus, Boolean reverse,
                                                    Pageable page) {

        var caller = userService.getCurrentUser();
        if (caller.isEmpty()) {
            return Page.empty();
        }
        var org = caller.get().getOrganization();

        // TODO Братишка разберись уже со спецификациями и фильтрациями
//        Specification<RequestToBank> filters = RequestToBankSpecs.withFilters(org, group, rhesus, List.of(RequestStatus.PENDING, RequestStatus.COMPLETED,
//                RequestStatus.REJECTED));
//        Sort sort = reverse != null && reverse ? page.getSort().descending() : page.getSort();
//        return requestToBankRepository.findAll(filters, PageRequest.of(page.getPageNumber(),
//                page.getPageSize(), sort));
        return requestToBankRepository.findAll(page);
    }

    public Page<RequestToBank> getRequestsForMed(RequestStatus status,
                                                 Pageable page) {

        var caller = userService.getCurrentUser();
        if (caller.isEmpty()) {
            return Page.empty();
        }
        var org = caller.get().getOrganization();
        // TODO Братишка разберись уже со спецификациями и фильтрациями
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
        }
        return "Партия крови успешно добавлена!";
    }
//
//    @Transactional
//    public String delete(Long id) {
//        return userService.getCurrentUser().map(user -> {
//            return bloodUnitRepository.findById(id).map(
//                    bloodUnit -> {
//                        if (Objects.equals(user.getOrganization().getId(), bloodUnit.getBloodBank().getId())) {
//                            bloodUnitRepository.delete(bloodUnit);
//                            return "Партия крови успешно удалена!";
//                        } else {
//                            return "Похоже эта партия принадлежит другому банку";
//                        }
//                    }
//            ).orElse("Не могу найти данную партию крови");
//        }).orElse("Не могу распознать вас");
//    }
//
//
//    @Transactional
//    public String update(BloodUnitDTOfrom dto) {
//        BloodUnit unit = dto.convert();
//        if (unit.getId() == null) {
//            throw new IllegalArgumentException("Для обновления организации не предоставлен id");
//        }
//        return bloodUnitRepository.findById(unit.getId()).map(un -> {
//            return userService.getCurrentUser().map(user -> {
//                unit.setBloodBank(user.getOrganization());
//                bloodUnitRepository.save(unit);
//                return "Партия крови успешно изменена!";
//            }).orElse("Не могу распознать вас");
//        }).orElse("Не могу найти данную партию крови");
//    }


}
