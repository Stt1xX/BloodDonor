package com.bloodlink.controllers;

import com.bloodlink.dto.mapper.NotificationMapper;
import com.bloodlink.dto.response.NotificationResponse;
import com.bloodlink.service.NotificationService;
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
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getUnreadNotifications(Pageable page) {
        log.debug("Getting unread notifications");
        Page<NotificationResponse> notifications = notificationService
                .getUnreadNotifications(page)
                .map(notificationMapper::toResponse);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Map<String, String>> readAllNotifications(
            @RequestBody List<Long> ids) {
        log.info("Marking {} notifications as read", ids.size());
        notificationService.readNotifications(ids);
        return ResponseEntity.ok(Map.of("message", "Уведомления успешно прочитаны"));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> readNotification(@PathVariable Long id) {
        log.info("Marking notification id={} as read", id);
        notificationService.readNotification(id);
        return ResponseEntity.ok(Map.of("message", "Уведомление успешно прочитано"));
    }
}
