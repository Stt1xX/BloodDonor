package com.bloodlink.controllers;

import com.bloodlink.entities.BankEmployee;
import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.DTOs.OrganizationDTOfrom;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.Role;
import com.bloodlink.exceptions.CustomDuplicateException;
import com.bloodlink.repositories.BankEmployeeRepository;
import com.bloodlink.service.BloodBankService;
import com.bloodlink.service.MedicalInstitutionService;
import com.bloodlink.service.ResourceUtilsService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/blood_units")
@RequiredArgsConstructor
public class BloodUnitResource {

    private final ResourceUtilsService resourceUtilsService;
    private final BankEmployeeRepository bankEmployeeRepository;

//    @RolesAllowed(value = {"BANK_EMPLOYEE"})
//    @GetMapping
//    public List<BloodUnit> getBloodUnits(@RequestParam String pattern, Pageable p) {
//        var caller = resourceUtilsService.getCaller();
//        Optional<BankEmployee> emplOpt = bankEmployeeRepository.findById(caller.getId());
//        if(emplOpt.isPresent()) {
//            throw new IllegalArgumentException("Вызывающий пользователь не является работником банка")
//        }
//
//    }

//    @RolesAllowed(value = {"ADMIN"})
//    @PostMapping
//    public ResponseEntity<?> addNewOrganization(@Valid @RequestBody OrganizationDTOfrom organizationDTOfrom) throws CustomDuplicateException {
//        if (organizationDTOfrom.getType() == OrganizationType.BLOOD_BANK) {
//            bloodBankService.save(organizationDTOfrom);
//            return ResponseEntity.ok("Банк крови успешно добавлен!");
//        } else if (organizationDTOfrom.getType() == OrganizationType.MEDICAL_INSTITUTION) {
//            medicalInstitutionService.save(organizationDTOfrom);
//            return ResponseEntity.ok("Медицинское учреждение успешно добавлено!");
//        } else {
//            return ResponseEntity.ok().body("Некорректный тип организации");
//        }
//    }

//    @RolesAllowed(value = {"ADMIN"})
//    @PutMapping
//    public ResponseEntity<?> updateOrganization(@Valid @RequestBody OrganizationDTOfrom organizationDTOfrom) throws CustomDuplicateException {
//        if (organizationDTOfrom.getType() == OrganizationType.BLOOD_BANK) {
//            bloodBankService.update(organizationDTOfrom);
//            return ResponseEntity.ok("Банк крови успешно обновлён!");
//        } else if (organizationDTOfrom.getType() == OrganizationType.MEDICAL_INSTITUTION) {
//            medicalInstitutionService.update(organizationDTOfrom);
//            return ResponseEntity.ok("Медицинское учреждение успешно обновлён!");
//        } else {
//            return ResponseEntity.ok().body("Некорректный тип организации");
//        }
//    }


}
