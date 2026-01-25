package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.RegistrationRequestDTOto;
import com.bloodlink.service.RegistrationRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/registration-requests")
@RequiredArgsConstructor
@Slf4j
public class RegistrationRequestController {

    private final RegistrationRequestService registrationRequestService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<RegistrationRequestDTOto>> getRequests(
            @RequestParam String pattern,
            Pageable page) {
        log.debug("Getting registration requests with pattern={}", pattern);
        return ResponseEntity.ok(registrationRequestService.getRequestsDto(pattern, page));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/approve")
    public ResponseEntity<Map<String, String>> approveRequest(@PathVariable Long id) {
        log.info("Approving registration request id={}", id);
        registrationRequestService.approveRequest(id);
        return ResponseEntity.ok(Map.of("message", "Заявка успешно одобрена"));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectRequest(@PathVariable Long id) {
        log.info("Rejecting registration request id={}", id);
        registrationRequestService.rejectRequest(id);
        return ResponseEntity.ok(Map.of("message", "Заявка успешно отклонена"));
    }
}
