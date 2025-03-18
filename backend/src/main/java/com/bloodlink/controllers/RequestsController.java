package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.RegistrationRequestDTOto;
import com.bloodlink.service.RegistrationRequestService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestsController {

    private final RegistrationRequestService registrationRequestService;

    @RolesAllowed(value = {"ADMIN"})
    @GetMapping
    public Page<RegistrationRequestDTOto> getRequests(@RequestParam String pattern,
                                                      Pageable p) {
        return registrationRequestService.getRequestsDto(pattern, p);
    }

    @RolesAllowed(value = {"ADMIN"})
    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        registrationRequestService.approveRequest(id);
        return ResponseEntity.ok().body("Заявка успешно одобрена");
    }

    @RolesAllowed(value = {"ADMIN"})
    @PostMapping("/reject/{id}")
    public ResponseEntity<?> rejectRequest(@PathVariable Long id) {
        registrationRequestService.rejectRequest(id);
        return ResponseEntity.ok().body("Заявка успешно отклонена");
    }

}