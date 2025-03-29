package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.RegistrationRequestDTOto;
import com.bloodlink.service.RegistrationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestsController {

    private final RegistrationRequestService registrationRequestService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public Page<RegistrationRequestDTOto> getRequests(@RequestParam String pattern,
                                                      Pageable p) {
        return registrationRequestService.getRequestsDto(pattern, p);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        registrationRequestService.approveRequest(id);
        return ResponseEntity.ok().body("Заявка успешно одобрена");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/reject/{id}")
    public ResponseEntity<?> rejectRequest(@PathVariable Long id) {
        registrationRequestService.rejectRequest(id);
        return ResponseEntity.ok().body("Заявка успешно отклонена");
    }

}