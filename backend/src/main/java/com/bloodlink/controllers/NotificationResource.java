package com.bloodlink.controllers;

import com.bloodlink.entities.DTOs.NotificationDTOto;
import com.bloodlink.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationResource {


    private final NotificationService notificationService;

    @GetMapping
    public Page<NotificationDTOto> getUnreadNotifications(Pageable page) {
        return notificationService.getUnreadNotifications(page).map(NotificationDTOto::convert);
    }

    @PostMapping("/read")
    public ResponseEntity<?> readNotifications(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(notificationService.readNotifications(ids));
    }


}
