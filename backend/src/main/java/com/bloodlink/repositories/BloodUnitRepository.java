package com.bloodlink.repositories;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BloodUnitRepository extends JpaRepository<BloodUnit, Long>, JpaSpecificationExecutor<BloodUnit> {
}
