package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.OrganizationDTOfrom;
import com.bloodlink.entities.DTOs.OrganizationDTOto;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.Role;
import com.bloodlink.entities.specifications.OrganizationRequestsSpecs;
import com.bloodlink.exceptions.CustomDuplicateException;
import com.bloodlink.service.BloodBankService;
import com.bloodlink.service.MedicalInstitutionService;
import com.bloodlink.utils.Utils;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationsResource {

    private final MedicalInstitutionService medicalInstitutionService;
    private final BloodBankService bloodBankService;

    @GetMapping
    @RequestMapping("/get_by_type")
    public ResponseEntity<?> getOrganizationsByType(@RequestParam @NotBlank String type, @RequestParam String pattern) {
        if (Role.fromString(type) == Role.MEDICAL_EMPLOYEE) {
            return ResponseEntity.ok(medicalInstitutionService.getAll(pattern));
        } else if (Role.fromString(type) == Role.BLOOD_BANK_EMPLOYEE) {
            return ResponseEntity.ok(bloodBankService.getAll(pattern));
        } else {
            return ResponseEntity.ok().body(new List[0]);
        }
    }

//    @GetMapping
//    public ResponseEntity<?> getOrganizations(@RequestParam String pattern, Pageable pageable) {
//        var page = Utils.mergeAndPaginate(medicalInstitutionService.getAll(pattern, pageable),
//                bloodBankService.getAll(pattern, pageable), pageable);
//        return ResponseEntity.ok(Utils.sortPage(page, OrganizationDTOto::getName));
//    }

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

    @RolesAllowed(value = {"ADMIN"})
    @PutMapping
    public ResponseEntity<?> updateOrganization(@Valid @RequestBody OrganizationDTOfrom organizationDTOfrom) throws CustomDuplicateException {
        if (organizationDTOfrom.getType() == OrganizationType.BLOOD_BANK) {
            bloodBankService.update(organizationDTOfrom);
            return ResponseEntity.ok("Банк крови успешно обновлён!");
        } else if (organizationDTOfrom.getType() == OrganizationType.MEDICAL_INSTITUTION) {
            medicalInstitutionService.update(organizationDTOfrom);
            return ResponseEntity.ok("Медицинское учреждение успешно обновлён!");
        } else {
            return ResponseEntity.ok().body("Некорректный тип организации");
        }
    }

//    public Page<OrganizationDTOto> getAll1(String pattern){
//        Specification<Organization> filters = Specification.where(!StringUtils.hasLength(pattern) ? null : OrganizationRequestsSpecs.nameLike(pattern))
//                .or(!StringUtils.hasLength(pattern) ? null : OrganizationRequestsSpecs.addressLike(pattern))
//                .or(!StringUtils.hasLength(pattern) ? null : OrganizationRequestsSpecs.phoneLike(pattern));
//
//        List<OrganizationDTOto> results = new ArrayList<>();
//
//        results.addAll(
//                bloodBankService.getAll(filters).stream()
//                        .map(org -> OrganizationDTOto.convert(org, OrganizationType.BLOOD_BANK))
//                        .toList()
//        );
//
//        results.addAll(
//                medicalInstitutionService.getAll(filters).stream()
//                        .map(org -> OrganizationDTOto.convert(org, OrganizationType.MEDICAL_INSTITUTION))
//                        .toList()
//        );
//
//    }

}
