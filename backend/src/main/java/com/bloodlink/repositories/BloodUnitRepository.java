package com.bloodlink.repositories;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BloodUnitRepository extends JpaRepository<BloodUnit, Long>, JpaSpecificationExecutor<BloodUnit> {
    Page<BloodUnit> getBloodUnitsByBloodBank(Organization bank, Pageable pageable);
}
