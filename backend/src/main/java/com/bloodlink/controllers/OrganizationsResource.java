package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.OrganizationDTOfrom;
import com.bloodlink.entities.enums.OrganizationType;
import com.bloodlink.entities.enums.Role;
import com.bloodlink.exceptions.CustomDuplicateException;
import com.bloodlink.service.OrganizationService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationsResource {

    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<?> getOrganization(@RequestParam String type,
                                            @RequestParam String pattern,
                                            Pageable page) {
        return ResponseEntity.ok(organizationService.getAll(type, pattern, page));
    }

    @RolesAllowed(value = {"ADMIN"})
    @PostMapping
    public ResponseEntity<?> addNewOrganization(@Valid @RequestBody OrganizationDTOfrom organizationDTOfrom) throws CustomDuplicateException {
        return ResponseEntity.ok(organizationService.save(organizationDTOfrom));
    }

    @RolesAllowed(value = {"ADMIN"})
    @PutMapping
    public ResponseEntity<?> updateOrganization(@Valid @RequestBody OrganizationDTOfrom organizationDTOfrom) throws CustomDuplicateException {
        return ResponseEntity.ok(organizationService.update(organizationDTOfrom));
    }

    @RolesAllowed(value = {"ADMIN"})
    @DeleteMapping
    public ResponseEntity<?> deleteOrganization(@RequestParam Long id) throws CustomDuplicateException {
        return ResponseEntity.ok(organizationService.delete(id));
    }
}
