package com.bloodlink.repositories;

import com.bloodlink.entities.BloodReserve;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BloodReserveRepository extends JpaRepository<BloodReserve, Long>, JpaSpecificationExecutor<BloodReserve> {
    @Query("SELECT b.bank FROM BloodReserve b WHERE " +
            "b.id.bloodGroup = :bloodGroup " +
            "AND b.id.rhFactor = :rhFactor " +
            "AND b.totalQuantity >= :totalQuantity")
    List<Organization> findByIdAndTotalQuantityGreaterThan
            (@Param("bloodGroup") BloodGroup bloodGroup,
             @Param("rhFactor") RhFactor rhFactor,
             @Param("totalQuantity") Double totalQuantity);
}
