package com.bloodlink.service;

import com.bloodlink.repositories.BloodUnitRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BloodUnitCleanupService {

    private final BloodUnitRepository bloodUnitRepository;

    public BloodUnitCleanupService(BloodUnitRepository bloodUnitRepository) {
        this.bloodUnitRepository = bloodUnitRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteExpiredBloodUnits() {
        int deletedCount = bloodUnitRepository.deleteExpiredUnits();
        // TODO Нужно доработать как минимум вызывать создания уведомления о том, что записи удалены
        System.out.println("Удалено " + deletedCount + " просроченных записей.");
    }
}

