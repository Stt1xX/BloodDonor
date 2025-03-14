package com.bloodlink.controllers;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.BloodType;
import com.bloodlink.entities.RhFactor;
import com.bloodlink.service.BloodUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/blood-units")
@RequiredArgsConstructor
public class BloodUnitController {
    private final BloodUnitService bloodUnitService;

    @GetMapping("/type")
    public List<BloodUnit> getByTypeAndRhFactor(
            @RequestParam BloodType bloodType,
            @RequestParam RhFactor rhFactor) {
        return bloodUnitService.findByTypeAndRhFactor(bloodType, rhFactor);
    }

    @GetMapping("/expiring")
    public List<BloodUnit> getExpiringUnits(
            @RequestParam(required = false) LocalDateTime before) {
        if (before == null) {
            before = LocalDateTime.now().plusDays(7); // По умолчанию проверяем на неделю вперед
        }
        return bloodUnitService.findExpiringUnits(before);
    }

    @GetMapping("/volume")
    public Double getTotalVolume(
            @RequestParam BloodType bloodType,
            @RequestParam RhFactor rhFactor,
            @RequestParam Long bankId) {
        return bloodUnitService.getTotalVolumeByTypeAndBank(bloodType, rhFactor, bankId);
    }

    @PostMapping
    public BloodUnit addBloodUnit(@RequestBody BloodUnit bloodUnit) {
        return bloodUnitService.save(bloodUnit);
    }

    @PutMapping("/{id}")
    public BloodUnit updateBloodUnit(
            @PathVariable Long id,
            @RequestBody BloodUnit bloodUnit) {
        bloodUnit.setId(id);
        return bloodUnitService.save(bloodUnit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBloodUnit(@PathVariable Long id) {
        bloodUnitService.delete(id);
        return ResponseEntity.ok().build();
    }
} 