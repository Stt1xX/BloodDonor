package com.bloodlink.service;

import com.bloodlink.dto.mapper.OrganizationMapper;
import com.bloodlink.dto.request.BloodRequestCreateRequest;
import com.bloodlink.dto.request.OrganizationCreateRequest;
import com.bloodlink.dto.response.OrganizationResponse;
import com.bloodlink.entities.BloodReserve;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.entities.enums.Role;
import com.bloodlink.entities.id.BloodReserveId;
import com.bloodlink.entities.specifications.OrganizationSpecs;
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
    private final OrganizationMapper organizationMapper;

    public Page<OrganizationResponse> getAll(String type, String pattern, Pageable page) {
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
        return p.map(organizationMapper::toResponse);
    }

    @Transactional
    public Organization save(OrganizationCreateRequest dto) {
        log.info("Creating organization: {}", dto.getName());
        
        if (dto.getType() != OrganizationType.BLOOD_BANK &&
            dto.getType() != OrganizationType.MEDICAL_INSTITUTION) {
            throw new ValidationException("Некорректный тип организации");
        }
        
        try {
            Organization org = organizationMapper.toEntity(dto);
            org = organizationRepository.save(org);
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
    public Organization update(OrganizationCreateRequest dto) {
        if (dto.getId() == null) {
            throw new ValidationException("Для обновления организации не предоставлен id");
        }
        
        Organization existing = organizationRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Организация не найдена: " + dto.getId()));
        
        log.info("Updating organization id={}", dto.getId());
        return save(dto);
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

    public List<OrganizationResponse> getBanksByResources(BloodRequestCreateRequest dto, String pattern){
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
                .map(organizationMapper::toResponse)
                .collect(Collectors.toList());
    }
}
