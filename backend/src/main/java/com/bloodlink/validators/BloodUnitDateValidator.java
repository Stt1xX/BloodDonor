package com.bloodlink.validators;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.DTOs.BloodUnitDTOfrom;
import com.bloodlink.validators.annotaions.ValidBloodUnitDates;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BloodUnitDateValidator implements ConstraintValidator<ValidBloodUnitDates, BloodUnitDTOfrom> {

    @Override
    public boolean isValid(BloodUnitDTOfrom bloodUnit, ConstraintValidatorContext context) {
        if (bloodUnit.getCreatedAt() == null || bloodUnit.getExpirationDate() == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate createdAt = bloodUnit.getCreatedAt();
        LocalDate expirationDate = bloodUnit.getExpirationDate();

        boolean isCreatedAtValid = !createdAt.isAfter(today);
        boolean isExpirationDateValid = expirationDate.isAfter(createdAt) && !expirationDate.isBefore(tomorrow);

        return isCreatedAtValid && isExpirationDateValid;
    }
}

