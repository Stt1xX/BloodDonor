package com.bloodlink.validators;

import com.bloodlink.validators.annotaions.WorkTimeCheckAnnotation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class WorkTimeCheckValidator implements ConstraintValidator<WorkTimeCheckAnnotation, Object> {

    private String minutesStart;
    private String minutesEnd;
    private String hoursStart;
    private String hoursEnd;


    @Override
    public void initialize(WorkTimeCheckAnnotation constraintAnnotation) {
        this.minutesStart = constraintAnnotation.minutesStart();
        this.minutesEnd = constraintAnnotation.minutesEnd();
        this.hoursStart = constraintAnnotation.hoursStart();
        this.hoursEnd = constraintAnnotation.hoursEnd();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {

            Field minutesStart = value.getClass().getDeclaredField(this.minutesStart);
            Field minutesEnd = value.getClass().getDeclaredField(this.minutesEnd);
            Field hoursStart = value.getClass().getDeclaredField(this.hoursStart);
            Field hoursEnd = value.getClass().getDeclaredField(this.hoursEnd);

            minutesEnd.setAccessible(true);
            minutesStart.setAccessible(true);
            hoursStart.setAccessible(true);
            hoursEnd.setAccessible(true);

            Integer minutesStartValue = (Integer) minutesStart.get(value);
            Integer minutesEndValue = (Integer) minutesEnd.get(value);
            Integer hoursStartValue = (Integer) hoursStart.get(value);
            Integer hoursEndValue = (Integer) hoursEnd.get(value);

            return (hoursStartValue * 60 + minutesStartValue) < (hoursEndValue * 60 + minutesEndValue);

        } catch (Exception e) {
            return false;
        }
    }
}