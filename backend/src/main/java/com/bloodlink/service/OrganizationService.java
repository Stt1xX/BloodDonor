package com.bloodlink.service;

import com.bloodlink.entities.DTOs.OrganizationDTOfrom;
import com.bloodlink.entities.DTOs.OrganizationDTOto;
import com.bloodlink.entities.Organization;
import com.bloodlink.exceptions.CustomDuplicateException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public abstract class OrganizationService<EntityClass extends Organization> {

    private final JpaRepository<EntityClass, Long> repository;
    private final JpaSpecificationExecutor<EntityClass> specificationExecutor;

    protected OrganizationService(JpaRepository<EntityClass, Long> repository, JpaSpecificationExecutor<EntityClass> specificationExecutor) {
        this.repository = repository;
        this.specificationExecutor = specificationExecutor;
    }

    public List<OrganizationDTOto> getAll(String pattern, Pageable p) {
        Specification<EntityClass> spec = (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), // Поле для поиска
                "%" + pattern.toLowerCase() + "%" // Шаблон поиска
        );
        Page<EntityClass> page = specificationExecutor.findAll(spec, p);
        return OrganizationDTOto.convertList(page.getContent());
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

    public EntityClass get(Long id) {
        return repository.findById(id).orElse(null);
    }
}
