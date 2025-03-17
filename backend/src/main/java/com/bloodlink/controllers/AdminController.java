package com.bloodlink.controllers;

import com.bloodlink.exceptions.CustomDuplicateException;
import com.bloodlink.service.BloodBankService;
import com.bloodlink.service.MedicalInstitutionService;
import jakarta.validation.Valid;
import com.bloodlink.entities.DTOs.OrganizationDTOto;
import com.bloodlink.entities.enums.OrganizationType;
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
    public ResponseEntity<?> addNewOrganization(@Valid @RequestBody OrganizationDTOto organizationDTOto) throws CustomDuplicateException {
        if(organizationDTOto.getType() == OrganizationType.BLOOD_BANK){
            bloodBankService.save(organizationDTOto);
            return ResponseEntity.ok("Банк крови успешно добавлен!");
        } else if(organizationDTOto.getType() == OrganizationType.MEDICAL_INSTITUTION){
            medicalInstitutionService.save(organizationDTOto);
            return ResponseEntity.ok("Медицинское учреждение успешно добавлено!");
        } else {
            return ResponseEntity.ok().body("Некорректный тип организации");
        }
    }
}