package com.bloodlink.service;

import com.bloodlink.entities.BloodRequest;
import com.bloodlink.entities.Notification;
import com.bloodlink.entities.Organization;
import com.bloodlink.entities.specifications.NotificationSpecs;
import com.bloodlink.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {


    private final UserService userService;
    private final NotificationRepository notificationRepositroy;

    public Page<Notification> getUnreadNotifications(Pageable page) {

        var callerOpt = userService.getCurrentUser();
        if (callerOpt.isEmpty()) {
            return Page.empty();
        }
        var caller = callerOpt.get();
        var filters = NotificationSpecs.withUser(caller).and(NotificationSpecs.isRead(Boolean.FALSE));
        return notificationRepositroy.findAll(filters, page);
    }

    @Transactional
    public String readNotifications(List<Long> ids) {

        var callerOpt = userService.getCurrentUser();
        if (callerOpt.isEmpty()) {
            return "Вы не авторизованы";
        }
        var caller = callerOpt.get();
        var filters = NotificationSpecs.withUser(caller).and(NotificationSpecs.isRead(Boolean.FALSE))
                .and(NotificationSpecs.withIdIn(ids));
        var notifications = notificationRepositroy.findAll(filters);
        for (var n : notifications) {
            n.setRead(Boolean.TRUE);
        }
        return "Уведомления успешно прочитаны";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void createNewRequestNotification(Organization bank, BloodRequest request) {
        for (var member : bank.getMembers()) {
            var notification = new Notification();
            notification.setUser(member);
            notification.setRead(false);
            notification.setNotification(String.format("Получен новый запрос: %s", request.getDescription()));
            notificationRepositroy.save(notification);
        }
    }


}
