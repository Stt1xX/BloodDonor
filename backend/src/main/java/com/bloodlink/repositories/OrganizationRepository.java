package com.bloodlink.repositories;

import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.OrganizationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface OrganizationRepository extends JpaRepository<Organization, Long>,
        JpaSpecificationExecutor<Organization> {

    Page<Organization> findAllByType(OrganizationType type, Specification<Organization> filters, Pageable page);
}