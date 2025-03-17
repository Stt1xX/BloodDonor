package com.bloodlink.service;

import com.bloodlink.entities.DTOs.OrganizationDTOto;
import com.bloodlink.entities.DTOs.OrganizationDTOfrom;
import com.bloodlink.entities.Organization;
import com.bloodlink.exceptions.CustomDuplicateException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class OrganizationService<EntityClass extends Organization> {

    private final JpaRepository<EntityClass, Long> repository;
    private final JpaSpecificationExecutor<EntityClass> specificationExecutor;

    protected OrganizationService(JpaRepository<EntityClass, Long> repository, JpaSpecificationExecutor<EntityClass> specificationExecutor) {
        this.repository = repository;
        this.specificationExecutor = specificationExecutor;
    }

    public List<OrganizationDTOfrom> getAll(String pattern) {
        Pageable pageable = PageRequest.of(0, 8);
        Specification<EntityClass> spec = (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")), // Поле для поиска
                "%" + pattern.toLowerCase() + "%" // Шаблон поиска
        );
        Page<EntityClass> page = specificationExecutor.findAll(spec, pageable);
        return OrganizationDTOfrom.convertList(page.getContent());
    }

    @Transactional
    public void save(OrganizationDTOto organizationDTOto) throws CustomDuplicateException {
        try {
            repository.save(organizationDTOto.getOrganization());
        } catch (DataIntegrityViolationException e) {
            throw new CustomDuplicateException("Такое заведение уже существует");
        }
    }

    public EntityClass get(Long id) {
        return repository.findById(id).orElse(null);
    }
}
