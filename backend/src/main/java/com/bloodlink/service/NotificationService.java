package com.bloodlink.service;

import com.bloodlink.entities.*;
import com.bloodlink.entities.enums.RequestStatus;
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
    private final NotificationRepository notificationRepository;

    public Page<Notification> getUnreadNotifications(Pageable page) {

        var callerOpt = userService.getCurrentUser();
        if (callerOpt.isEmpty()) {
            return Page.empty();
        }
        var caller = callerOpt.get();
        var filters = NotificationSpecs.withUser(caller).and(NotificationSpecs.isRead(Boolean.FALSE));
        return notificationRepository.findAll(filters, page);
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
        var notifications = notificationRepository.findAll(filters);
        for (var n : notifications) {
            n.setRead(Boolean.TRUE);
        }
        return "Уведомления успешно прочитаны";
    }

    @Transactional
    public String readNotification(Long id) {

        var callerOpt = userService.getCurrentUser();
        if (callerOpt.isEmpty()) {
            return "Вы не авторизованы";
        }
        var caller = callerOpt.get();
        var filters = NotificationSpecs.withUser(caller).and(NotificationSpecs.isRead(Boolean.FALSE))
                .and(NotificationSpecs.withIdIn(List.of(id)));
        var notifications = notificationRepository.findAll(filters);
        if (notifications.isEmpty()) {
            return "Я не нашел такого уведомления";
        }
        notifications.getFirst().setRead(Boolean.TRUE);
        return "Уведомление успешно прочитано";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void createNewRequestNotification(Organization bank, BloodRequest request) {
        for (var member : bank.getMembers()) {
            var notification = new Notification();
            notification.setUser(member);
            notification.setRead(false);
            notification.setNotification(String.format("Получен новый запрос: %sгр., rh%s, %sл.",
                    request.getBloodGroup().getSymbol(), request.getRhFactor().getSymbol(), request.getVolumeNeeded()));
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
            unit.getBloodGroup().getSymbol(), unit.getRhFactor().getSymbol(), unit.getVolume()));

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
                    request.getRequest().getBloodGroup().getSymbol(), request.getRequest().getRhFactor().getSymbol(), request.getRequest().getVolumeNeeded());
            notification.setNotification(retStr);

            notificationRepository.save(notification);
        }
    }


}
