package com.bloodlink.controllers;

import com.bloodlink.dto.request.BloodUnitCreateRequest;
import com.bloodlink.dto.response.BloodUnitResponse;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.service.BloodUnitsService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/blood-units")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Blood Units", description = "API для управления партиями крови")
public class BloodUnitController {

    private final BloodUnitsService bloodUnitsService;

    @Operation(
            summary = "Получить партии крови",
            description = "Возвращает пагинированный список партий крови банка с фильтрацией"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Нет доступа (требуется роль BANK_EMPLOYEE)")
    })
    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getBloodUnits(
            @Parameter(description = "Группа крови (1-4 или null для всех)") @RequestParam String bloodGroup,
            @Parameter(description = "Резус-фактор (POSITIVE/NEGATIVE или null)") @RequestParam String rhesusFactor,
            @Parameter(description = "Обратная сортировка") @RequestParam Boolean reverse,
            Pageable page) {
        log.debug("Getting blood units: bloodGroup={}, rh={}", bloodGroup, rhesusFactor);
        var group = BloodGroup.fromValue(bloodGroup);
        var factor = RhFactor.fromValue(rhesusFactor);
        
        Page<BloodUnitResponse> unitsPage = bloodUnitsService.getOrganizationBloodUnits(group, factor, reverse, page);
        Double summaryVolume = bloodUnitsService.getSummaryVolume(group, factor);
        
        return ResponseEntity.ok(Map.of(
                "page", unitsPage,
                "summaryVolume", summaryVolume
        ));
    }

    @Operation(summary = "Добавить партию крови")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Партия создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Map<String, String>> addBloodUnit(
            @RequestBody @Valid BloodUnitCreateRequest dto) {
        log.info("Creating new blood unit: bloodGroup={}, rh={}, volume={}", 
                dto.getBloodGroup(), dto.getRhesusFactor(), dto.getVolume());
        var created = bloodUnitsService.save(dto);
        URI location = URI.create("/api/blood-units/" + created.getId());
        return ResponseEntity.created(location).body(Map.of("message", "Партия крови успешно добавлена!"));
    }

    @Operation(summary = "Удалить партию крови")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Партия удалена"),
            @ApiResponse(responseCode = "404", description = "Партия не найдена")
    })
    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeBloodUnit(
            @Parameter(description = "ID партии") @PathVariable Long id) {
        log.info("Deleting blood unit id={}", id);
        bloodUnitsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить партию крови")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Партия обновлена"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "404", description = "Партия не найдена")
    })
    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateBloodUnit(
            @Parameter(description = "ID партии") @PathVariable Long id,
            @Valid @RequestBody BloodUnitCreateRequest dto) {
        log.info("Updating blood unit id={}", id);
        bloodUnitsService.update(dto);
        return ResponseEntity.ok(Map.of("message", "Партия крови успешно изменена!"));
    }
}
