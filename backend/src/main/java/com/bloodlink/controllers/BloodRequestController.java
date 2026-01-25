package com.bloodlink.controllers;

import com.bloodlink.dto.mapper.BloodRequestMapper;
import com.bloodlink.dto.request.BloodRequestCreateRequest;
import com.bloodlink.dto.response.BloodRequestBankResponse;
import com.bloodlink.dto.response.BloodRequestMedicalResponse;
import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.enums.BloodGroup;
import com.bloodlink.entities.enums.RequestStatus;
import com.bloodlink.entities.enums.RhFactor;
import com.bloodlink.service.BloodRequestsService;
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
@RequestMapping("/api/blood-requests")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Blood Requests", description = "API для управления запросами крови")
public class BloodRequestController {

    private final BloodRequestsService bloodRequestsService;
    private final BloodRequestMapper bloodRequestMapper;

    @Operation(
            summary = "Получить запросы крови для банка",
            description = "Возвращает пагинированный список запросов крови с фильтрацией по группе и резусу"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Нет доступа (требуется роль BANK_EMPLOYEE)")
    })
    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @GetMapping("/bank")
    public ResponseEntity<Page<BloodRequestBankResponse>> getBankBloodRequests(
            @Parameter(description = "Группа крови (1-4 или null для всех)") @RequestParam String bloodGroup,
            @Parameter(description = "Резус-фактор (POSITIVE/NEGATIVE или null)") @RequestParam String rhesusFactor,
            @Parameter(description = "Обратная сортировка") @RequestParam Boolean reverse,
            Pageable page) {
        log.debug("Getting blood requests for bank: bloodGroup={}, rh={}", bloodGroup, rhesusFactor);
        var group = BloodGroup.fromValue(bloodGroup);
        var factor = RhFactor.fromValue(rhesusFactor);
        Page<BloodRequestBankResponse> result = bloodRequestsService
                .getRequestsForBanker(group, factor, reverse, page)
                .map(bloodRequestMapper::toBankResponse);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Получить запросы крови для медучреждения",
            description = "Возвращает пагинированный список запросов крови текущего пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Нет доступа (требуется роль MEDICAL_EMPLOYEE)")
    })
    @PreAuthorize("hasAnyAuthority('MEDICAL_EMPLOYEE')")
    @GetMapping("/medical")
    public ResponseEntity<Page<BloodRequestMedicalResponse>> getMedBloodRequests(
            @Parameter(description = "Статус запроса (PENDING/ACCEPTED/REJECTED или null)") @RequestParam String status,
            Pageable page) {
        log.debug("Getting blood requests for medical: status={}", status);
        var st = RequestStatus.fromName(status);
        Page<BloodRequestMedicalResponse> result = bloodRequestsService
                .getRequestsForMed(st, page)
                .map(bloodRequestMapper::toMedicalResponse);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Принять запрос крови")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос принят"),
            @ApiResponse(responseCode = "404", description = "Запрос не найден"),
            @ApiResponse(responseCode = "422", description = "Невозможно принять запрос")
    })
    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @PatchMapping("/{id}/accept")
    public ResponseEntity<Map<String, String>> acceptBloodRequest(
            @Parameter(description = "ID запроса") @PathVariable Long id) {
        log.info("Accepting blood request id={}", id);
        bloodRequestsService.accept(id);
        return ResponseEntity.ok(Map.of("message", "Заявка успешно принята"));
    }

    @Operation(summary = "Отклонить запрос крови")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос отклонён"),
            @ApiResponse(responseCode = "404", description = "Запрос не найден")
    })
    @PreAuthorize("hasAnyAuthority('BANK_EMPLOYEE')")
    @PatchMapping("/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectBloodRequest(
            @Parameter(description = "ID запроса") @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        log.info("Rejecting blood request id={}, reason={}", id, reason);
        bloodRequestsService.reject(id, reason);
        return ResponseEntity.ok(Map.of("message", "Заявка успешно отклонена"));
    }

    @Operation(summary = "Создать запрос крови")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Запрос создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @PreAuthorize("hasAnyAuthority('MEDICAL_EMPLOYEE')")
    @PostMapping
    public ResponseEntity<Map<String, String>> addBloodRequest(
            @RequestBody @Valid BloodRequestCreateRequest dto) {
        log.info("Creating new blood request: bloodGroup={}, rh={}", 
                dto.getBloodGroup(), dto.getRhesusFactor());
        BloodRequest created = bloodRequestsService.save(dto);
        URI location = URI.create("/api/blood-requests/" + created.getId());
        return ResponseEntity.created(location).body(Map.of("message", "Запрос на кровь успешно добавлен!"));
    }
}
