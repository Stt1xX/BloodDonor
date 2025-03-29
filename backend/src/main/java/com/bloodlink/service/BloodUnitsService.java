package com.bloodlink.service;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.DTOs.BloodUnitDTOfrom;
import com.bloodlink.entities.DTOs.BloodUnitDTOto;
import com.bloodlink.entities.DTOs.OrganizationDTOfrom;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.User;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.entities.specifications.BloodUnitsSpecs;
import com.bloodlink.repositories.BloodUnitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BloodUnitsService {

    private final UserService userService;
    private final BloodUnitRepository bloodUnitRepository;

    public Page<BloodUnitDTOto> getOrganizationBloodUnits(BloodGroup group, RhFactor rhesus, Boolean reverse, Pageable page){

        Specification<BloodUnit> filters = Specification.where(
                group == null ? null : BloodUnitsSpecs.hasBloodType(group))
                .and(rhesus == null ? null : BloodUnitsSpecs.hasRhFactor(rhesus));

        Optional<User> opt = userService.getCurrentUser();
        User user;
        if (opt.isPresent()) {
            user = opt.get();
            filters.and(BloodUnitsSpecs.hasOrganization(user.getOrganization()));
            Sort sort = reverse != null && reverse ? page.getSort().descending() : page.getSort();
            var units = bloodUnitRepository.findAll(filters,
                    PageRequest.of(page.getPageNumber(), page.getPageSize(), sort));
            return units.map(BloodUnitDTOto::convert);
        } else {
            return Page.empty();
        }
    }

    public Double getSummaryVolume(BloodGroup group, RhFactor rhesus){
        Specification<BloodUnit> filters = Specification.where(
                        group == null ? null : BloodUnitsSpecs.hasBloodType(group))
                .and(rhesus == null ? null : BloodUnitsSpecs.hasRhFactor(rhesus));
        Optional<User> opt = userService.getCurrentUser();
        User user;
        if (opt.isPresent()) {
            user = opt.get();
            filters.and(BloodUnitsSpecs.hasOrganization(user.getOrganization()));
            return bloodUnitRepository.findAll(filters)
                    .stream().map(BloodUnit::getVolume).reduce(0.0, Double::sum);
        }
        return 0.0;
    }

    @Transactional
    public String save(BloodUnitDTOfrom dto) {
        BloodUnit unit = dto.convert();
        return userService.getCurrentUser().map(user -> {
            unit.setBloodBank(user.getOrganization());
            bloodUnitRepository.save(unit);
            return "Партия крови успешно добавлена!";
        }).orElse("Не могу распознать вас");
    }

    @Transactional
    public String delete(Long id){
        return userService.getCurrentUser().map(user -> {
            return bloodUnitRepository.findById(id).map(
                    bloodUnit -> {
                        if (Objects.equals(user.getOrganization().getId(), bloodUnit.getBloodBank().getId())) {
                            bloodUnitRepository.delete(bloodUnit);
                            return "Партия крови успешно удалена!";
                        } else {
                            return "Похоже эта партия принадлежит другому банку";
                        }
                    }
            ).orElse("Не могу найти данную партию крови");
        }).orElse("Не могу распознать вас");
    }


    @Transactional
    public String update(BloodUnitDTOfrom dto) {
        BloodUnit unit = dto.convert();
        if (unit.getId() == null){
            throw new IllegalArgumentException("Для обновления организации не предоставлен id");
        }
        return bloodUnitRepository.findById(unit.getId()).map(un -> {
            return userService.getCurrentUser().map(user -> {
                unit.setBloodBank(user.getOrganization());
                bloodUnitRepository.save(unit);
                return "Партия крови успешно изменена!";
            }).orElse("Не могу распознать вас");
        }).orElse("Не могу найти данную партию крови");
    }


}
