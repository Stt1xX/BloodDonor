package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.OrganizationDTO;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.service.BloodBankService;
import com.bloodlink.service.MedicalInstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api")
@RequiredArgsConstructor
public class AdminController {

    private final BloodBankService bloodBankService;
    private final MedicalInstitutionService medicalInstitutionService;

    @PostMapping("/add_organization")
    public ResponseEntity<?> addNewOrganization(@RequestBody OrganizationDTO organizationDTO){
        if(organizationDTO.getType() == OrganizationType.BLOOD_BANK){
            return bloodBankService.save(organizationDTO);
        } else if(organizationDTO.getType() == OrganizationType.MEDICAL_INSTITUTION){
            return medicalInstitutionService.save(organizationDTO);
        } else {
            return ResponseEntity.badRequest().body("Некорректный тип организации");
        }
    }
}