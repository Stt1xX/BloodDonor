package com.bloodlink.service;

import com.bloodlink.entities.BloodBank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class BloodBankService extends OrganizationService<BloodBank> {

    protected BloodBankService(JpaRepository<BloodBank, Long> repository, JpaSpecificationExecutor<BloodBank> specificationExecutor) {
        super(repository, specificationExecutor);
    }
}