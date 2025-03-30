package com.bloodlink.repositories;

import com.bloodlink.entities.BloodUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BloodUnitRepository extends JpaRepository<BloodUnit, Long>, JpaSpecificationExecutor<BloodUnit> {

    @Query("SELECT b FROM BloodUnit b WHERE b.expirationDate < CURRENT_DATE")
    List<BloodUnit> findExpiredUnits();

    @Modifying
    @Transactional
    @Query("DELETE FROM BloodUnit b WHERE b.expirationDate < CURRENT_DATE")
    int deleteExpiredUnits();
}
