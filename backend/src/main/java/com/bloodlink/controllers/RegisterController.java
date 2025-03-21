package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.RegistrationRequestDTOfrom;
import com.bloodlink.entities.DTOs.RegistrationRequestDTOto;
import com.bloodlink.service.RegistrationRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterController {
    private final RegistrationRequestService registrationRequestService;

    @PostMapping("/requests")
    public ResponseEntity<?> createUserRequest(@Valid @RequestBody RegistrationRequestDTOfrom request) {
        return ResponseEntity.ok().body(registrationRequestService.save(request));
    }
}
