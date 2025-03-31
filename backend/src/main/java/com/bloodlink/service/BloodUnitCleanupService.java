package com.bloodlink.service;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.repositories.BloodUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodUnitCleanupService {

    private final BloodUnitRepository bloodUnitRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteExpiredBloodUnits() {
        List<BloodUnit> bloodUnits = bloodUnitRepository.findExpiredUnits();
        bloodUnitRepository.deleteExpiredUnits();
        for (BloodUnit bloodUnit : bloodUnits) {
            notificationService.createNewExpirationNotification(bloodUnit);
        }
    }
}

