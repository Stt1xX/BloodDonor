package com.bloodlink.controllers;

import com.bloodlink.dto.request.BloodRequestCreateRequest;
import com.bloodlink.dto.request.OrganizationCreateRequest;
import com.bloodlink.dto.response.OrganizationResponse;
import com.bloodlink.entities.Organization;
import com.bloodlink.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Organizations", description = "API для управления организациями")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Operation(
            summary = "Получить список организаций",
            description = "Возвращает пагинированный список организаций с фильтрацией по типу и названию"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно")
    })
    @GetMapping
    public ResponseEntity<Page<OrganizationResponse>> getOrganizations(
            @Parameter(description = "Тип организации (BLOOD_BANK/MEDICAL или пустая строка)") @RequestParam String type,
            @Parameter(description = "Поисковый паттерн по названию") @RequestParam String pattern,
            Pageable page) {
        log.debug("Getting organizations with type={}, pattern={}", type, pattern);
        return ResponseEntity.ok(organizationService.getAll(type, pattern, page));
    }

    @Operation(
            summary = "Получить банки крови с подходящими запасами",
            description = "Возвращает список банков крови, имеющих запасы нужной группы и резуса"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно")
    })
    @PostMapping("/banks-with-resources")
    public ResponseEntity<List<OrganizationResponse>> getBanksWithResources(
            @RequestBody BloodRequestCreateRequest dto,
            @Parameter(description = "Поисковый паттерн по названию") @RequestParam String pattern) {
        log.debug("Getting banks with resources for blood group={}, rh={}", 
                dto.getBloodGroup(), dto.getRhesusFactor());
        return ResponseEntity.ok(organizationService.getBanksByResources(dto, pattern));
    }

    @Operation(summary = "Создать организацию")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Организация создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "403", description = "Нет доступа (требуется роль ADMIN)")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Map<String, String>> addNewOrganization(
            @Valid @RequestBody OrganizationCreateRequest organizationDto) {
        log.info("Creating new organization: {}", organizationDto.getName());
        Organization created = organizationService.save(organizationDto);
        URI location = URI.create("/api/organizations/" + created.getId());
        return ResponseEntity.created(location).body(Map.of("message", "Организация успешно добавлена"));
    }

    @Operation(summary = "Обновить организацию")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Организация обновлена"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Организация не найдена")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateOrganization(
            @Parameter(description = "ID организации") @PathVariable Long id,
            @Valid @RequestBody OrganizationCreateRequest organizationDto) {
        log.info("Updating organization id={}", id);
        organizationService.update(organizationDto);
        return ResponseEntity.ok(Map.of("message", "Информация о заведении обновлена"));
    }

    @Operation(summary = "Удалить организацию")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Организация удалена"),
            @ApiResponse(responseCode = "404", description = "Организация не найдена")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(
            @Parameter(description = "ID организации") @PathVariable Long id) {
        log.info("Deleting organization id={}", id);
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
