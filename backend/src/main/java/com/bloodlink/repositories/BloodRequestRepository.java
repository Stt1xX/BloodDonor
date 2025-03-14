package com.bloodlink.repositories;

import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    List<BloodRequest> findByBloodBankIdAndStatus(Long bloodBankId, RequestStatus status);
    
    List<BloodRequest> findByMedicalInstitutionId(Long medicalInstitutionId);
    
    List<BloodRequest> findByMedicalInstitutionIdAndStatus(Long medicalInstitutionId, RequestStatus status);
    
    List<BloodRequest> findByStatusAndUrgentOrderByCreatedAtAsc(RequestStatus status, Boolean urgent);
} 