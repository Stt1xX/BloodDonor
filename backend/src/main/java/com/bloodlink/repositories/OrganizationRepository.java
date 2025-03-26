package com.bloodlink.repositories;

import com.bloodlink.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface OrganizationRepository extends JpaRepository<Organization, Long>,
        JpaSpecificationExecutor<Organization> {
}