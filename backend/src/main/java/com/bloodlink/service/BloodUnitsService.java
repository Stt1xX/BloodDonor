package com.bloodlink.service;

import com.bloodlink.entities.BloodReserve;
import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.DTOs.BloodUnitDTOfrom;
import com.bloodlink.entities.DTOs.BloodUnitDTOto;
import com.bloodlink.entities.User;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.entities.specifications.BloodReserveSpecs;
import com.bloodlink.entities.specifications.BloodUnitsSpecs;
import com.bloodlink.exceptions.AccessDeniedException;
import com.bloodlink.exceptions.BusinessException;
import com.bloodlink.exceptions.ResourceNotFoundException;
import com.bloodlink.repositories.BloodReserveRepository;
import com.bloodlink.repositories.BloodUnitRepository;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class BloodUnitsService {

    private final UserService userService;
    private final BloodUnitRepository bloodUnitRepository;
    private final BloodReserveRepository bloodReserveRepository;

    public Page<BloodUnitDTOto> getOrganizationBloodUnits(BloodGroup group, RhFactor rhesus, Boolean reverse, Pageable page) {
        User user = userService.getCurrentUser();
        
        Specification<BloodUnit> filters = Specification.where(
                        group == null ? null : BloodUnitsSpecs.hasBloodType(group))
                .and(rhesus == null ? null : BloodUnitsSpecs.hasRhFactor(rhesus))
                .and(BloodUnitsSpecs.hasOrganization(user.getOrganization()));

        Sort sort = reverse != null && reverse ? page.getSort().descending() : page.getSort();
        var units = bloodUnitRepository.findAll(filters,
                PageRequest.of(page.getPageNumber(), page.getPageSize(), sort));
        return units.map(BloodUnitDTOto::convert);
    }

    public Double getSummaryVolume(BloodGroup group, RhFactor rhesus) {
        User user = userService.getCurrentUser();
        
        Specification<BloodReserve> filters = Specification.where(
                        group == null ? null : BloodReserveSpecs.hasBloodType(group))
                .and(rhesus == null ? null : BloodReserveSpecs.hasRhFactor(rhesus))
                .and(BloodReserveSpecs.hasBank(user.getOrganization()));
        
        return bloodReserveRepository.findAll(filters)
                .stream()
                .map(BloodReserve::getTotalQuantity)
                .reduce(0.0, Double::sum);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Retryable(include = {SQLException.class})
    public void fillBloodRequest(BloodGroup group, RhFactor rhesus, Double volume) {
        User user = userService.getCurrentUser();
        log.info("Filling blood request: group={}, rh={}, volume={}, bank={}", 
                group, rhesus, volume, user.getOrganization().getName());
        
        var filters = BloodUnitsSpecs.hasBloodType(group)
                .and(BloodUnitsSpecs.hasRhFactor(rhesus))
                .and(BloodUnitsSpecs.hasOrganization(user.getOrganization()))
                .and(BloodUnitsSpecs.isFresherThan(LocalDateTime.now()));
        
        var units = bloodUnitRepository.findAll(filters, Sort.by("expirationDate").and(Sort.by("volume")));
        
        for (var unit : units) {
            if (unit.getVolume() > volume) {
                unit.setVolume(unit.getVolume() - volume);
                bloodUnitRepository.save(unit);
                log.info("Blood request filled successfully");
                return;
            }
            volume -= unit.getVolume();
            bloodUnitRepository.delete(unit);
        }
        
        if (volume > 0) {
            throw new BusinessException("Не хватает крови для выполнения запроса");
        }
    }

    @Transactional
    public BloodUnit save(BloodUnitDTOfrom dto) {
        User user = userService.getCurrentUser();
        log.info("Creating blood unit: group={}, rh={}, volume={}", 
                dto.getBloodGroup(), dto.getRhesusFactor(), dto.getVolume());
        
        BloodUnit unit = dto.convert();
        unit.setBloodBank(user.getOrganization());
        return bloodUnitRepository.save(unit);
    }

    @Transactional
    public void delete(Long id) {
        User user = userService.getCurrentUser();
        
        BloodUnit bloodUnit = bloodUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Партия крови не найдена: " + id));
        
        if (!Objects.equals(user.getOrganization().getId(), bloodUnit.getBloodBank().getId())) {
            throw new AccessDeniedException("Эта партия принадлежит другому банку");
        }
        
        log.info("Deleting blood unit id={}", id);
        bloodUnitRepository.delete(bloodUnit);
    }

    @Transactional
    public BloodUnit update(BloodUnitDTOfrom dto) {
        if (dto.getId() == null) {
            throw new BusinessException("Для обновления партии крови не предоставлен id");
        }
        
        User user = userService.getCurrentUser();
        
        BloodUnit existingUnit = bloodUnitRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Партия крови не найдена: " + dto.getId()));
        
        if (!Objects.equals(user.getOrganization().getId(), existingUnit.getBloodBank().getId())) {
            throw new AccessDeniedException("Эта партия принадлежит другому банку");
        }
        
        log.info("Updating blood unit id={}", dto.getId());
        BloodUnit unit = dto.convert();
        unit.setBloodBank(user.getOrganization());
        return bloodUnitRepository.save(unit);
    }

    @Transactional
    public int deleteExpiredUnits() {
        log.info("Deleting expired blood units");
        int deleted = bloodUnitRepository.deleteExpiredUnits();
        log.info("Deleted {} expired blood units", deleted);
        return deleted;
    }
}
