package com.bloodlink.service;

import com.bloodlink.entities.BloodReserve;
import com.bloodlink.entities.DTOs.BloodRequestDTOfrom;
import com.bloodlink.entities.DTOs.OrganizationDTOfrom;
import com.bloodlink.entities.DTOs.OrganizationDTOto;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.entities.enums.Role;
import com.bloodlink.entities.id.BloodReserveId;
import com.bloodlink.entities.specifications.OrganizationSpecs;
import com.bloodlink.exceptions.BusinessException;
import com.bloodlink.exceptions.ConflictException;
import com.bloodlink.exceptions.ResourceNotFoundException;
import com.bloodlink.exceptions.ValidationException;
import com.bloodlink.repositories.BloodReserveRepository;
import com.bloodlink.repositories.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final BloodReserveRepository bloodReserveRepository;

    public Page<OrganizationDTOto> getAll(String type, String pattern, Pageable page) {
        OrganizationType typeEnum = null;
        var role = Role.fromString(type);
        if (role != null) {
            typeEnum = switch (role) {
                case ADMIN -> null;
                case MEDICAL_EMPLOYEE -> OrganizationType.MEDICAL_INSTITUTION;
                case BANK_EMPLOYEE -> OrganizationType.BLOOD_BANK;
            };
        }

        Specification<Organization> textFilters = StringUtils.hasLength(pattern)
                ? OrganizationSpecs.nameLike(pattern)
                .or(OrganizationSpecs.addressLike(pattern))
                .or(OrganizationSpecs.phoneLike(pattern))
                : null;

        Specification<Organization> filters = Specification.where(textFilters)
                .and(OrganizationSpecs.hasType(typeEnum));

        var p = organizationRepository.findAll(filters, page);
        return p.map(OrganizationDTOto::convert);
    }

    @Transactional
    public Organization save(OrganizationDTOfrom organizationDTOfrom) {
        log.info("Creating organization: {}", organizationDTOfrom.getName());
        
        if (organizationDTOfrom.getType() != OrganizationType.BLOOD_BANK &&
            organizationDTOfrom.getType() != OrganizationType.MEDICAL_INSTITUTION) {
            throw new ValidationException("Некорректный тип организации");
        }
        
        try {
            Organization org = organizationRepository.save(organizationDTOfrom.convert());
            createBloodReservesForBank(org);
            log.info("Organization created id={}", org.getId());
            return org;
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Заведение с таким именем уже существует");
        }
    }

    @Transactional
    public void createBloodReservesForBank(Organization bank) {
        if (bank.getType() != OrganizationType.BLOOD_BANK) {
            return;
        }
        List<BloodReserve> reserves = new ArrayList<>();
        for (BloodGroup group : BloodGroup.values()) {
            for (RhFactor factor : RhFactor.values()) {
                BloodReserveId reserveId = new BloodReserveId(group, factor, bank.getId());
                BloodReserve reserve = new BloodReserve(reserveId, bank, 0.0);
                reserves.add(reserve);
            }
        }
        bloodReserveRepository.saveAll(reserves);
    }

    @Transactional
    public Organization update(OrganizationDTOfrom organizationDTOfrom) {
        if (organizationDTOfrom.getId() == null) {
            throw new ValidationException("Для обновления организации не предоставлен id");
        }
        
        Organization existing = organizationRepository.findById(organizationDTOfrom.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Организация не найдена: " + organizationDTOfrom.getId()));
        
        log.info("Updating organization id={}", organizationDTOfrom.getId());
        return save(organizationDTOfrom);
    }

    @Transactional
    public void delete(Long id) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Организация не найдена: " + id));
        
        log.info("Deleting organization id={}", id);
        organizationRepository.delete(org);
    }

    public Organization get(Long id) {
        return id == null ? null : organizationRepository.findById(id).orElse(null);
    }

    public List<OrganizationDTOto> getBanksByResources(BloodRequestDTOfrom dto, String pattern){
        if (dto.getBloodGroup() == null || dto.getRhesusFactor() == null || dto.getVolumeNeeded() == null){
            return List.of();
        }
        var banksWithResources = bloodReserveRepository.findByIdAndTotalQuantityGreaterThan(dto.getBloodGroup(), dto.getRhesusFactor(), dto.getVolumeNeeded());
        Specification<Organization> textFilters = StringUtils.hasLength(pattern)
                ? OrganizationSpecs.nameLike(pattern)
                .or(OrganizationSpecs.addressLike(pattern))
                .or(OrganizationSpecs.phoneLike(pattern))
                : null;
        Specification<Organization> filters = Specification.where(textFilters)
                .and(OrganizationSpecs.hasType(OrganizationType.BLOOD_BANK));

        return organizationRepository.findAll(filters).stream()
                .filter(banksWithResources::contains)
                .map(OrganizationDTOto::convert)
                .collect(Collectors.toList());
    }
}
