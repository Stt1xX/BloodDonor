package com.bloodlink.validators.annotaions;

import com.bloodlink.validators.BloodUnitDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;


@Constraint(validatedBy = BloodUnitDateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBloodUnitDates {
    String message() default "Некорректные даты: дата поставки должна быть сегодня или раньше, а дата просрочки должна быть позже даты поставки и минимум завтра";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
