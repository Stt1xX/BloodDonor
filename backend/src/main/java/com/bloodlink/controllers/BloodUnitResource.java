package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.BloodUnitDTOfrom;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.service.BloodUnitsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/blood_units")
@RequiredArgsConstructor
public class BloodUnitResource {

    private final BloodUnitsService bloodUnitsService;

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @GetMapping
    public Map<String, Object>  getBloodUnits(@RequestParam String bloodGroup, @RequestParam String rhesusFactor,
                                              @RequestParam Boolean reverse, Pageable page){
        var group = BloodGroup.fromSymbol(bloodGroup);
        var factor = RhFactor.fromSymbol(rhesusFactor);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("page", bloodUnitsService.getOrganizationBloodUnits(group, factor, reverse, page));
        retMap.put("summaryVolume", bloodUnitsService.getSummaryVolume(group, factor));
        return retMap;
    }

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @PostMapping
    public ResponseEntity<?> addBloodUnit(@RequestBody @Valid BloodUnitDTOfrom dto) {
        return ResponseEntity.ok(bloodUnitsService.save(dto));
    }

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @DeleteMapping
    public ResponseEntity<?> removeBloodUnit(@RequestParam Long id) {
        return ResponseEntity.ok(bloodUnitsService.delete(id));
    }

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @PutMapping
    public ResponseEntity<?> updateOrganization(@Valid @RequestBody BloodUnitDTOfrom dto) {
        return ResponseEntity.ok(bloodUnitsService.update(dto));
    }

}
