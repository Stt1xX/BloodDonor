package com.bloodlink.service;

import com.bloodlink.entities.*;
import com.bloodlink.entities.enums.RequestStatus;
import com.bloodlink.entities.specifications.NotificationSpecs;
import com.bloodlink.exceptions.ResourceNotFoundException;
import com.bloodlink.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationService {

    private final UserService userService;
    private final NotificationRepository notificationRepository;

    public Page<Notification> getUnreadNotifications(Pageable page) {
        User caller = userService.getCurrentUser();
        var filters = NotificationSpecs.withUser(caller).and(NotificationSpecs.isRead(Boolean.FALSE));
        return notificationRepository.findAll(filters, page);
    }

    @Transactional
    public void readNotifications(List<Long> ids) {
        User caller = userService.getCurrentUser();
        log.info("Marking {} notifications as read for user={}", ids.size(), caller.getEmail());
        
        var filters = NotificationSpecs.withUser(caller)
                .and(NotificationSpecs.isRead(Boolean.FALSE))
                .and(NotificationSpecs.withIdIn(ids));
        var notifications = notificationRepository.findAll(filters);
        
        for (var n : notifications) {
            n.setRead(Boolean.TRUE);
        }
    }

    @Transactional
    public void readNotification(Long id) {
        User caller = userService.getCurrentUser();
        log.info("Marking notification id={} as read for user={}", id, caller.getEmail());
        
        var filters = NotificationSpecs.withUser(caller)
                .and(NotificationSpecs.isRead(Boolean.FALSE))
                .and(NotificationSpecs.withIdIn(List.of(id)));
        var notifications = notificationRepository.findAll(filters);
        
        if (notifications.isEmpty()) {
            throw new ResourceNotFoundException("Уведомление не найдено: " + id);
        }
        notifications.getFirst().setRead(Boolean.TRUE);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void createNewRequestNotification(Organization bank, BloodRequest request) {
        for (var member : bank.getMembers()) {
            var notification = new Notification();
            notification.setUser(member);
            notification.setRead(false);
            notification.setNotification(String.format("Получен новый запрос: %sгр., rh%s, %sл.",
                    request.getBloodGroup().getValue(), request.getRhFactor().getDisplaySymbol(), request.getVolumeNeeded()));
            notificationRepository.save(notification);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void createNewExpirationNotification(BloodUnit unit) {
        for (var member : unit.getBloodBank().getMembers()) {
            var notification = new Notification();
            notification.setUser(member);
            notification.setRead(false);
            notification.setNotification(String.format("Партия крови устарела, я ее удалил: %sгр., rh%s, %sл.",
            unit.getBloodGroup().getValue(), unit.getRhFactor().getDisplaySymbol(), unit.getVolume()));

            notificationRepository.save(notification);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void createChangeStatusNotification(RequestToBank request) {
        for (var member : request.getRequest().getMedicalInstitution().getMembers()) {
            var notification = new Notification();
            notification.setUser(member);
            notification.setRead(false);
            String retStr = String.format(request.getStatus() == RequestStatus.COMPLETED ?
                            "Вам одобрили заявку: %sгр., rh%s, %sл." :
                            "Вам отклонили заявку: %sгр., rh%s, %sл.",
                    request.getRequest().getBloodGroup().getValue(), request.getRequest().getRhFactor().getDisplaySymbol(), request.getRequest().getVolumeNeeded());
            notification.setNotification(retStr);

            notificationRepository.save(notification);
        }
    }


}
