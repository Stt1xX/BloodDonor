package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.BloodRequestDTOfrom;
import com.bloodlink.entities.DTOs.BloodRequestDTOtoBank;
import com.bloodlink.entities.DTOs.BloodRequestDTOtoMed;
import com.bloodlink.entities.DTOs.BloodUnitDTOfrom;
import com.bloodlink.entities.enums.BloodGroup;
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
                                                            @RequestParam Boolean reverse, @RequestParam Boolean isEmergency, Pageable page) {
        var group = BloodGroup.fromSymbol(bloodGroup);
        var factor = RhFactor.fromSymbol(rhesusFactor);
        return bloodRequestsService.getRequests(group, factor, reverse, isEmergency, page)
                .map(BloodRequestDTOtoBank::convert);
    }

    @PreAuthorize("hasAnyAuthority('MEDICAL_EMPLOYEE')")
    @GetMapping("/medical")
    public Page<BloodRequestDTOtoMed> getMedBloodRequests(@RequestParam String bloodGroup,
                                                          @RequestParam String rhesusFactor,
                                                          @RequestParam Boolean reverse, @RequestParam Boolean isEmergency, Pageable page) {
        var group = BloodGroup.fromSymbol(bloodGroup);
        var factor = RhFactor.fromSymbol(rhesusFactor);
        return bloodRequestsService.getRequests(group, factor, reverse, isEmergency, page)
                .map(BloodRequestDTOtoMed::convert);
    }

    @PreAuthorize("hasAnyAuthority('MEDICAL_EMPLOYEE')")
    @PostMapping
    public ResponseEntity<?> addBloodRequest(@RequestBody @Valid BloodRequestDTOfrom dto) {
        try {
            return ResponseEntity.ok(bloodRequestsService.save(dto));
        } catch(IllegalArgumentException e){
            return ResponseEntity.ok(e.getMessage());
        }
    }
//
//    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
//    @DeleteMapping
//    public ResponseEntity<?> removeBloodUnit(@RequestParam Long id) {
//        return ResponseEntity.ok(bloodUnitsService.delete(id));
//    }
//
//    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
//    @PutMapping
//    public ResponseEntity<?> updateOrganization(@Valid @RequestBody BloodUnitDTOfrom dto) {
//        return ResponseEntity.ok(bloodUnitsService.update(dto));
//    }

}
