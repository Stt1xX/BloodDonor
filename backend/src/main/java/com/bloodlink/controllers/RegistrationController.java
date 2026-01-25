package com.bloodlink.controllers;

import com.bloodlink.dto.request.RegistrationCreateRequest;
import com.bloodlink.entities.RegistrationRequest;
import com.bloodlink.service.RegistrationRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final RegistrationRequestService registrationRequestService;

    @PostMapping("/requests")
    public ResponseEntity<Map<String, String>> createUserRequest(
            @Valid @RequestBody RegistrationCreateRequest request) {
        log.info("Creating registration request for email={}", request.getEmail());
        RegistrationRequest created = registrationRequestService.save(request);
        URI location = URI.create("/api/registration-requests/" + created.getId());
        return ResponseEntity.created(location).body(Map.of("message", "Заявка на регистрацию успешно подана!"));
    }
}
