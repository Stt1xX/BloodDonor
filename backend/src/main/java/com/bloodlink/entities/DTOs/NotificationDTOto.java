package com.bloodlink.entities.DTOs;

import com.bloodlink.entities.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationDTOto {

    private Long id;
    private boolean isRead;
    private String notification;
    private LocalDateTime createdAt;

    public static NotificationDTOto convert(Notification notification) {
        return new NotificationDTOto(notification.getId(), notification.isRead(), notification.getNotification(), notification.getCreatedAt());
    }
}
