package com.bloodlink.repositories;

import com.bloodlink.entities.BankEmployee;
import com.bloodlink.entities.BloodBank;
import com.bloodlink.entities.BloodUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BloodUnitRepository extends JpaRepository<BloodUnit, Long>, JpaSpecificationExecutor<BloodUnit> {
    Page<BloodUnit> getBloodUnitsByBloodBank(BloodBank bank, Pageable pageable);
}
