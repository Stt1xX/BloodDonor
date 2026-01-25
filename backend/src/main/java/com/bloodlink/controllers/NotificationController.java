package com.bloodlink.controllers;

import com.bloodlink.dto.mapper.NotificationMapper;
import com.bloodlink.dto.response.NotificationResponse;
import com.bloodlink.service.NotificationService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notifications", description = "API для управления уведомлениями")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @Operation(
            summary = "Получить непрочитанные уведомления",
            description = "Возвращает пагинированный список непрочитанных уведомлений текущего пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getUnreadNotifications(Pageable page) {
        log.debug("Getting unread notifications");
        Page<NotificationResponse> notifications = notificationService
                .getUnreadNotifications(page)
                .map(notificationMapper::toResponse);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Пометить несколько уведомлений как прочитанные")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Уведомления помечены"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @PatchMapping("/read-all")
    public ResponseEntity<Map<String, String>> readAllNotifications(
            @RequestBody List<Long> ids) {
        log.info("Marking {} notifications as read", ids.size());
        notificationService.readNotifications(ids);
        return ResponseEntity.ok(Map.of("message", "Уведомления успешно прочитаны"));
    }

    @Operation(summary = "Пометить уведомление как прочитанное")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Уведомление помечено"),
            @ApiResponse(responseCode = "404", description = "Уведомление не найдено")
    })
    @PatchMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> readNotification(
            @Parameter(description = "ID уведомления") @PathVariable Long id) {
        log.info("Marking notification id={} as read", id);
        notificationService.readNotification(id);
        return ResponseEntity.ok(Map.of("message", "Уведомление успешно прочитано"));
    }
}
