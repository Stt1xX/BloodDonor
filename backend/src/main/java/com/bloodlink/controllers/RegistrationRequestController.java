package com.bloodlink.controllers;

import com.bloodlink.dto.response.RegistrationRequestResponse;
import com.bloodlink.service.RegistrationRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/registration-requests")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Registration Requests", description = "API для управления заявками на регистрацию")
public class RegistrationRequestController {

    private final RegistrationRequestService registrationRequestService;

    @Operation(
            summary = "Получить заявки на регистрацию",
            description = "Возвращает пагинированный список заявок на регистрацию с поиском"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "403", description = "Нет доступа (требуется роль ADMIN)")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<RegistrationRequestResponse>> getRequests(
            @Parameter(description = "Поисковый паттерн") @RequestParam String pattern,
            Pageable page) {
        log.debug("Getting registration requests with pattern={}", pattern);
        return ResponseEntity.ok(registrationRequestService.getRequestsDto(pattern, page));
    }

    @Operation(summary = "Одобрить заявку на регистрацию")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заявка одобрена"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/approve")
    public ResponseEntity<Map<String, String>> approveRequest(
            @Parameter(description = "ID заявки") @PathVariable Long id) {
        log.info("Approving registration request id={}", id);
        registrationRequestService.approveRequest(id);
        return ResponseEntity.ok(Map.of("message", "Заявка успешно одобрена"));
    }

    @Operation(summary = "Отклонить заявку на регистрацию")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заявка отклонена"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectRequest(
            @Parameter(description = "ID заявки") @PathVariable Long id) {
        log.info("Rejecting registration request id={}", id);
        registrationRequestService.rejectRequest(id);
        return ResponseEntity.ok(Map.of("message", "Заявка успешно отклонена"));
    }
}
