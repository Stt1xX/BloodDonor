package com.bloodlink.service;

import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.RequestStatus;
import com.bloodlink.repositories.BloodRequestRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodRequestService {
    private final BloodRequestRepository bloodRequestRepository;

    @Transactional(readOnly = true)
    public List<BloodRequest> findByBloodBankAndStatus(Long bloodBankId, RequestStatus status) {
        return bloodRequestRepository.findByBloodBankIdAndStatus(bloodBankId, status);
    }

    @Transactional(readOnly = true)
    public List<BloodRequest> findByMedicalInstitution(Long medicalInstitutionId) {
        return bloodRequestRepository.findByMedicalInstitutionId(medicalInstitutionId);
    }

    @Transactional(readOnly = true)
    public List<BloodRequest> findByMedicalInstitutionAndStatus(Long medicalInstitutionId, RequestStatus status) {
        return bloodRequestRepository.findByMedicalInstitutionIdAndStatus(medicalInstitutionId, status);
    }

    @Transactional(readOnly = true)
    public List<BloodRequest> findPendingRequests(Boolean urgent) {
        return bloodRequestRepository.findByStatusAndUrgentOrderByCreatedAtAsc(RequestStatus.PENDING, urgent);
    }

    @Transactional
    public BloodRequest createRequest(BloodRequest request) {
        request.setStatus(RequestStatus.PENDING);
        return bloodRequestRepository.save(request);
    }

    @Transactional
    public BloodRequest approveRequest(Long requestId) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));
        request.setStatus(RequestStatus.APPROVED);
        return bloodRequestRepository.save(request);
    }

    @Transactional
    public BloodRequest rejectRequest(Long requestId, String reason) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));
        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason);
        return bloodRequestRepository.save(request);
    }

    @Transactional
    public BloodRequest completeRequest(Long requestId) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));
        request.setStatus(RequestStatus.COMPLETED);
        return bloodRequestRepository.save(request);
    }
} 