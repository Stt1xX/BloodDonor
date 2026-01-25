package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.NotificationDTOto;
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

    @GetMapping
    public ResponseEntity<Page<NotificationDTOto>> getUnreadNotifications(Pageable page) {
        log.debug("Getting unread notifications");
        Page<NotificationDTOto> notifications = notificationService
                .getUnreadNotifications(page)
                .map(NotificationDTOto::convert);
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
