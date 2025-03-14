package com.bloodlink.controllers;

import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.RequestStatus;
import com.bloodlink.service.BloodRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-requests")
@RequiredArgsConstructor
public class BloodRequestController {
    private final BloodRequestService bloodRequestService;

    @GetMapping("/bank/{bankId}")
    public List<BloodRequest> getBankRequests(
            @PathVariable Long bankId,
            @RequestParam(required = false) RequestStatus status) {
        return bloodRequestService.findByBloodBankAndStatus(bankId, status);
    }

    @GetMapping("/medical/{medicalId}")
    public List<BloodRequest> getMedicalInstitutionRequests(
            @PathVariable Long medicalId,
            @RequestParam(required = false) RequestStatus status) {
        if (status != null) {
            return bloodRequestService.findByMedicalInstitutionAndStatus(medicalId, status);
        }
        return bloodRequestService.findByMedicalInstitution(medicalId);
    }

    @GetMapping("/pending")
    public List<BloodRequest> getPendingRequests(
            @RequestParam(required = false, defaultValue = "false") Boolean urgent) {
        return bloodRequestService.findPendingRequests(urgent);
    }

    @PostMapping
    public BloodRequest createRequest(@RequestBody BloodRequest request) {
        return bloodRequestService.createRequest(request);
    }

    @PutMapping("/{id}/approve")
    public BloodRequest approveRequest(@PathVariable Long id) {
        return bloodRequestService.approveRequest(id);
    }

    @PutMapping("/{id}/reject")
    public BloodRequest rejectRequest(
            @PathVariable Long id,
            @RequestParam String reason) {
        return bloodRequestService.rejectRequest(id, reason);
    }

    @PutMapping("/{id}/complete")
    public BloodRequest completeRequest(@PathVariable Long id) {
        return bloodRequestService.completeRequest(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        // TODO: Implement delete logic if needed
        return ResponseEntity.ok().build();
    }
} 