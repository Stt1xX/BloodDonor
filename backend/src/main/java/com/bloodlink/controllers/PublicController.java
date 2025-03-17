package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.RegistrationRequestDTOto;
import com.bloodlink.entities.enums.Role;
import com.bloodlink.service.BloodBankService;
import com.bloodlink.service.MedicalInstitutionService;
import com.bloodlink.service.RegistrationRequestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final MedicalInstitutionService medicalInstitutionService;
    private final BloodBankService bloodBankService;
    private final RegistrationRequestService registrationRequestService;

    @GetMapping("/get_all_organization")
    public ResponseEntity<?> getAllOrganizations(@RequestParam @NotBlank String type, @RequestParam @NotBlank String pattern) {
        if (Role.fromString(type) == Role.MEDICAL_EMPLOYEE) {
            return ResponseEntity.ok(medicalInstitutionService.getAll(pattern));
        } else if (Role.fromString(type) == Role.BLOOD_BANK_EMPLOYEE) {
            return ResponseEntity.ok(bloodBankService.getAll(pattern));
        } else {
            return ResponseEntity.ok().body(new List[0]);
        }
    }

    @PostMapping("/create_user_request")
        public ResponseEntity<?> createUserRequest(@Valid @RequestBody RegistrationRequestDTOto request) {
        registrationRequestService.save(request);
        return ResponseEntity.ok().body("Заявка на создание пользователя успешно подана!");
    }
}
