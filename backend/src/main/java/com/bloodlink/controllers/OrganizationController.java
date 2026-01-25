package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.BloodRequestDTOfrom;
import com.bloodlink.entities.DTOs.OrganizationDTOfrom;
import com.bloodlink.entities.DTOs.OrganizationDTOto;
import com.bloodlink.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
@Slf4j
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<Page<OrganizationDTOto>> getOrganizations(
            @RequestParam String type,
            @RequestParam String pattern,
            Pageable page) {
        log.debug("Getting organizations with type={}, pattern={}", type, pattern);
        return ResponseEntity.ok(organizationService.getAll(type, pattern, page));
    }

    @PostMapping("/banks-with-resources")
    public ResponseEntity<List<OrganizationDTOto>> getBanksWithResources(
            @RequestBody BloodRequestDTOfrom dto,
            @RequestParam String pattern) {
        log.debug("Getting banks with resources for blood group={}, rh={}", 
                dto.getBloodGroup(), dto.getRhesusFactor());
        return ResponseEntity.ok(organizationService.getBanksByResources(dto, pattern));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<String> addNewOrganization(
            @Valid @RequestBody OrganizationDTOfrom organizationDto) {
        log.info("Creating new organization: {}", organizationDto.getName());
        String result = organizationService.save(organizationDto);
        return ResponseEntity.status(201).body(result);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationDTOfrom organizationDto) {
        log.info("Updating organization id={}", id);
        return ResponseEntity.ok(organizationService.update(organizationDto));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        log.info("Deleting organization id={}", id);
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
