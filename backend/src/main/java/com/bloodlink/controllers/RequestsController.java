package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.RegistrationRequestDTOto;
import com.bloodlink.service.RegistrationRequestService;
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

    public Page<RegistrationRequestDTOto> getRequests(@RequestParam String name, @RequestParam String surname,
                                                      @RequestParam String email, @RequestParam String role,
                                                      Pageable p) {
        return registrationRequestService.getRequestsDto(name, surname, email, role, p);
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        registrationRequestService.approveRequest(id);
        return ResponseEntity.ok().body("Заявка успешно одобрена");
    }

    @PostMapping("/requests/reject/{id}")
    public ResponseEntity<?> rejectRequest(@PathVariable Long id) {
        registrationRequestService.rejectRequest(id);
        return ResponseEntity.ok().body("Заявка успешно отклонена");
    }

}
