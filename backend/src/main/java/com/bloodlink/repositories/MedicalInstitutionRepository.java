package com.bloodlink.repositories;

import com.bloodlink.entities.MedicalInstitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalInstitutionRepository extends JpaRepository<MedicalInstitution, Long>,
        JpaSpecificationExecutor<MedicalInstitution> {
} 