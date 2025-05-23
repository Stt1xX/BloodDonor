package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.BloodRequestDTOfrom;
import com.bloodlink.entities.DTOs.BloodRequestDTOtoBank;
import com.bloodlink.entities.DTOs.BloodRequestDTOtoMed;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RequestStatus;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.service.BloodRequestsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blood_requests")
@RequiredArgsConstructor
public class BloodRequestResource {

    private final BloodRequestsService bloodRequestsService;

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @GetMapping("/bank")
    public Page<BloodRequestDTOtoBank> getBankBloodRequests(@RequestParam String bloodGroup,
                                                            @RequestParam String rhesusFactor,
                                                            @RequestParam Boolean reverse, Pageable page) {
        var group = BloodGroup.fromSymbol(bloodGroup);
        var factor = RhFactor.fromSymbol(rhesusFactor);
        return bloodRequestsService.getRequestsForBanker(group, factor, reverse, page)
                .map(BloodRequestDTOtoBank::convert);
    }

    @PreAuthorize("hasAnyAuthority('MEDICAL_EMPLOYEE')")
    @GetMapping("/medical")
    public Page<BloodRequestDTOtoMed> getMedBloodRequests(@RequestParam String status, Pageable page) {
        var st = RequestStatus.fromName(status);
        return bloodRequestsService.getRequestsForMed(st, page)
                .map(BloodRequestDTOtoMed::convert);
    }

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @PostMapping("/accept/{id}")
    public ResponseEntity<?> acceptBloodRequest(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bloodRequestsService.accept(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @PostMapping("/reject/{id}")
    public ResponseEntity<?> rejectBloodRequest(@PathVariable Long id, @RequestBody String reason) {
        try {
            System.out.println(reason);
            return ResponseEntity.ok(bloodRequestsService.reject(id, reason));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MEDICAL_EMPLOYEE')")
    @PostMapping
    public ResponseEntity<?> addBloodRequest(@RequestBody @Valid BloodRequestDTOfrom dto) {
        try {
            return ResponseEntity.ok(bloodRequestsService.save(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }
}
