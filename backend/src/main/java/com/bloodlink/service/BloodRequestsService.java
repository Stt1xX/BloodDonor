package com.bloodlink.service;

import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.DTOs.BloodRequestDTOfrom;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.RequestStatus;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.entities.specifications.BloodRequestSpecs;
import com.bloodlink.repositories.BloodRequestRepository;
import com.bloodlink.repositories.OrganizationRepository;
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

@RequiredArgsConstructor
@Service
public class BloodRequestsService {

    private final UserService userService;
    private final BloodRequestRepository bloodRequestRepository;
    private final OrganizationRepository organizationRepository;

    public Page<BloodRequest> getRequests(BloodGroup group, RhFactor rhesus, Boolean reverse, Boolean isEmergency,
                                          Pageable page) {

        var caller = userService.getCurrentUser();
        if (caller.isEmpty()) {
            return Page.empty();
        }
        var org = caller.get().getOrganization();

        Specification<BloodRequest> filters = Specification.where(
                        group == null ? null : BloodRequestSpecs.hasBloodType(group))
                .and(rhesus == null ? null : BloodRequestSpecs.hasRhFactor(rhesus))
                .and(isEmergency == null ? null : BloodRequestSpecs.isEmergency(isEmergency))
                .and(BloodRequestSpecs.hasOrganization(org))
                .and(BloodRequestSpecs.hasAnyStatus(List.of(RequestStatus.PENDING, RequestStatus.COMPLETED,
                        RequestStatus.REJECTED)));

        Sort sort = reverse != null && reverse ? page.getSort().descending() : page.getSort();
        var requests = bloodRequestRepository.findAll(filters, PageRequest.of(page.getPageNumber(),
                page.getPageSize(), sort));
        return requests;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = IllegalArgumentException.class)
    @Retryable(include = {SQLException.class})
    public String save(BloodRequestDTOfrom dto) {
        var caller = userService.getCurrentUser();
        if (caller.isEmpty()) {
            return "Не могу распознать вас";
        }
        var org = caller.get().getOrganization();
        Long groupId = null;

        for (var bankId : dto.getBloodBanks()) {
            //Might better be cloned
            BloodRequest request = dto.convert(org);
            if (groupId != null) request.setRequestGroupId(groupId);
            var bankOpt = organizationRepository.findById(bankId);
            if (bankOpt.isEmpty()) {
                throw new IllegalArgumentException("Указанная организация не существует");
            }
            if (bankOpt.get().getType() != OrganizationType.BLOOD_BANK) {
                throw new IllegalArgumentException("Указанная организация не является банком");
            }

            var bank = bankOpt.get();
            request.setBloodBank(bank);
            request = bloodRequestRepository.save(request);
            if (groupId == null) groupId = request.getRequestGroupId();
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
