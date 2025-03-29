package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.OrganizationDTOfrom;
import com.bloodlink.exceptions.CustomDuplicateException;
import com.bloodlink.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationsResource {

    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<?> getOrganizations(@RequestParam String type,
                                            @RequestParam String pattern,
                                            Pageable page) {
        return ResponseEntity.ok(organizationService.getAll(type, pattern, page));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addNewOrganization(@Valid @RequestBody OrganizationDTOfrom organizationDTOfrom) {
        return ResponseEntity.ok(organizationService.save(organizationDTOfrom));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateOrganization(@Valid @RequestBody OrganizationDTOfrom organizationDTOfrom) {
        return ResponseEntity.ok(organizationService.update(organizationDTOfrom));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> deleteOrganization(@RequestParam Long id){
        return ResponseEntity.ok(organizationService.delete(id));
    }
}
