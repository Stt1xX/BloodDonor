package com.bloodlink.controllers;

import com.bloodlink.dto.request.BloodUnitCreateRequest;
import com.bloodlink.dto.response.BloodUnitResponse;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.service.BloodUnitsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/blood-units")
@RequiredArgsConstructor
@Slf4j
public class BloodUnitController {

    private final BloodUnitsService bloodUnitsService;

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getBloodUnits(
            @RequestParam String bloodGroup,
            @RequestParam String rhesusFactor,
            @RequestParam Boolean reverse,
            Pageable page) {
        log.debug("Getting blood units: bloodGroup={}, rh={}", bloodGroup, rhesusFactor);
        var group = BloodGroup.fromValue(bloodGroup);
        var factor = RhFactor.fromValue(rhesusFactor);
        
        Page<BloodUnitResponse> unitsPage = bloodUnitsService.getOrganizationBloodUnits(group, factor, reverse, page);
        Double summaryVolume = bloodUnitsService.getSummaryVolume(group, factor);
        
        return ResponseEntity.ok(Map.of(
                "page", unitsPage,
                "summaryVolume", summaryVolume
        ));
    }

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Map<String, String>> addBloodUnit(
            @RequestBody @Valid BloodUnitCreateRequest dto) {
        log.info("Creating new blood unit: bloodGroup={}, rh={}, volume={}", 
                dto.getBloodGroup(), dto.getRhesusFactor(), dto.getVolume());
        var created = bloodUnitsService.save(dto);
        URI location = URI.create("/api/blood-units/" + created.getId());
        return ResponseEntity.created(location).body(Map.of("message", "Партия крови успешно добавлена!"));
    }

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeBloodUnit(@PathVariable Long id) {
        log.info("Deleting blood unit id={}", id);
        bloodUnitsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateBloodUnit(
            @PathVariable Long id,
            @Valid @RequestBody BloodUnitCreateRequest dto) {
        log.info("Updating blood unit id={}", id);
        bloodUnitsService.update(dto);
        return ResponseEntity.ok(Map.of("message", "Партия крови успешно изменена!"));
    }
}
