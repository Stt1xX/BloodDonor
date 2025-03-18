package com.bloodlink.service;

import com.bloodlink.entities.BloodBank;
import com.bloodlink.entities.DTOs.OrganizationDTOfrom;
import com.bloodlink.entities.DTOs.OrganizationDTOto;
import com.bloodlink.entities.MedicalInstitution;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.RegistrationRequest;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.specifications.OrganizationRequestsSpecs;
import com.bloodlink.entities.specifications.RegistrationRequestsSpecs;
import com.bloodlink.exceptions.CustomDuplicateException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class OrganizationService<EntityClass extends Organization> {

    private final JpaRepository<EntityClass, Long> repository;
    private final JpaSpecificationExecutor<EntityClass> specificationExecutor;

    protected OrganizationService(JpaRepository<EntityClass, Long> repository, JpaSpecificationExecutor<EntityClass> specificationExecutor) {
        this.repository = repository;
        this.specificationExecutor = specificationExecutor;
    }

    public Page<OrganizationDTOto> getAll(String pattern) {
        Specification<EntityClass> spec = (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                "%" + pattern.toLowerCase() + "%"
        );
        var page = specificationExecutor.findAll(spec, PageRequest.of(0, 8));

        return page.map(entity -> switch (entity) {
            case BloodBank org -> OrganizationDTOto.convert(org, OrganizationType.BLOOD_BANK);
            case MedicalInstitution org -> OrganizationDTOto.convert(org, OrganizationType.MEDICAL_INSTITUTION);
            default -> throw new IllegalArgumentException("Unknown entity type: " + entity.getClass());
        });
    }

    @Transactional
    public void save(OrganizationDTOfrom organizationDTOfrom) throws CustomDuplicateException {
        try {
            repository.save(organizationDTOfrom.getOrganization());
        } catch (DataIntegrityViolationException e) {
            throw new CustomDuplicateException("Такое заведение уже существует");
        }
    }

    @Transactional
    public void update(OrganizationDTOfrom organizationDTOfrom) throws CustomDuplicateException {
        if (organizationDTOfrom.getId() == null) {
            throw new IllegalArgumentException("Для обновления организации не предоставлен id");
        }
        Optional<EntityClass> orgOpt = repository.findById(organizationDTOfrom.getId());
        if (orgOpt.isEmpty()) {
            throw new IllegalArgumentException("Организация с таким id не существует");
        }
        try {
            repository.save(organizationDTOfrom.getOrganization());
        } catch (DataIntegrityViolationException e) {
            throw new CustomDuplicateException("Такое заведение уже существует");
        }
    }

    public List<OrganizationDTOto> getAll(Specification<EntityClass> filters) {
        return specificationExecutor.findAll(filters).stream()
                .map(entity -> switch (entity) {
                    case BloodBank org -> OrganizationDTOto.convert(org, OrganizationType.BLOOD_BANK);
                    case MedicalInstitution org -> OrganizationDTOto.convert(org, OrganizationType.MEDICAL_INSTITUTION);
                    default -> throw new IllegalArgumentException("Unknown entity type: " + entity.getClass());
                })
                .toList();
    }

    public EntityClass get(Long id) {
        return repository.findById(id).orElse(null);
    }
}
