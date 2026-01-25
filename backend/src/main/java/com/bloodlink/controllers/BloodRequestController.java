package com.bloodlink.controllers;

import com.bloodlink.dto.mapper.BloodRequestMapper;
import com.bloodlink.dto.request.BloodRequestCreateRequest;
import com.bloodlink.dto.response.BloodRequestBankResponse;
import com.bloodlink.dto.response.BloodRequestMedicalResponse;
import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RequestStatus;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.service.BloodRequestsService;
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
@RequestMapping("/api/blood-requests")
@RequiredArgsConstructor
@Slf4j
public class BloodRequestController {

    private final BloodRequestsService bloodRequestsService;
    private final BloodRequestMapper bloodRequestMapper;

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @GetMapping("/bank")
    public ResponseEntity<Page<BloodRequestBankResponse>> getBankBloodRequests(
            @RequestParam String bloodGroup,
            @RequestParam String rhesusFactor,
            @RequestParam Boolean reverse,
            Pageable page) {
        log.debug("Getting blood requests for bank: bloodGroup={}, rh={}", bloodGroup, rhesusFactor);
        var group = BloodGroup.fromValue(bloodGroup);
        var factor = RhFactor.fromValue(rhesusFactor);
        Page<BloodRequestBankResponse> result = bloodRequestsService
                .getRequestsForBanker(group, factor, reverse, page)
                .map(bloodRequestMapper::toBankResponse);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyAuthority('MEDICAL_EMPLOYEE')")
    @GetMapping("/medical")
    public ResponseEntity<Page<BloodRequestMedicalResponse>> getMedBloodRequests(
            @RequestParam String status,
            Pageable page) {
        log.debug("Getting blood requests for medical: status={}", status);
        var st = RequestStatus.fromName(status);
        Page<BloodRequestMedicalResponse> result = bloodRequestsService
                .getRequestsForMed(st, page)
                .map(bloodRequestMapper::toMedicalResponse);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @PatchMapping("/{id}/accept")
    public ResponseEntity<Map<String, String>> acceptBloodRequest(@PathVariable Long id) {
        log.info("Accepting blood request id={}", id);
        bloodRequestsService.accept(id);
        return ResponseEntity.ok(Map.of("message", "Заявка успешно принята"));
    }

    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @PatchMapping("/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectBloodRequest(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        log.info("Rejecting blood request id={}, reason={}", id, reason);
        bloodRequestsService.reject(id, reason);
        return ResponseEntity.ok(Map.of("message", "Заявка успешно отклонена"));
    }

    @PreAuthorize("hasAnyAuthority('MEDICAL_EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Map<String, String>> addBloodRequest(
            @RequestBody @Valid BloodRequestCreateRequest dto) {
        log.info("Creating new blood request: bloodGroup={}, rh={}", 
                dto.getBloodGroup(), dto.getRhesusFactor());
        BloodRequest created = bloodRequestsService.save(dto);
        URI location = URI.create("/api/blood-requests/" + created.getId());
        return ResponseEntity.created(location).body(Map.of("message", "Запрос на кровь успешно добавлен!"));
    }
}
