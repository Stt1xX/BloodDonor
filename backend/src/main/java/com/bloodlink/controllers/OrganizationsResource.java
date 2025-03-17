package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.OrganizationDTOfrom;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.Role;
import com.bloodlink.exceptions.CustomDuplicateException;
import com.bloodlink.service.BloodBankService;
import com.bloodlink.service.MedicalInstitutionService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationsResource {

    private final MedicalInstitutionService medicalInstitutionService;
    private final BloodBankService bloodBankService;

    @GetMapping
    public ResponseEntity<?> getAllOrganizations(@RequestParam @NotBlank String type, @RequestParam @NotBlank String pattern) {
        if (Role.fromString(type) == Role.MEDICAL_EMPLOYEE) {
            return ResponseEntity.ok(medicalInstitutionService.getAll(pattern));
        } else if (Role.fromString(type) == Role.BLOOD_BANK_EMPLOYEE) {
            return ResponseEntity.ok(bloodBankService.getAll(pattern));
        } else {
            return ResponseEntity.ok().body(new List[0]);
        }
    }

    @RolesAllowed(value = {"ADMIN"})
    @PostMapping
    public ResponseEntity<?> addNewOrganization(@Valid @RequestBody OrganizationDTOfrom organizationDTOfrom) throws CustomDuplicateException {
        if (organizationDTOfrom.getType() == OrganizationType.BLOOD_BANK) {
            bloodBankService.save(organizationDTOfrom);
            return ResponseEntity.ok("Банк крови успешно добавлен!");
        } else if (organizationDTOfrom.getType() == OrganizationType.MEDICAL_INSTITUTION) {
            medicalInstitutionService.save(organizationDTOfrom);
            return ResponseEntity.ok("Медицинское учреждение успешно добавлено!");
        } else {
            return ResponseEntity.ok().body("Некорректный тип организации");
        }
    }

}
