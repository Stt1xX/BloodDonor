package com.bloodlink.validators.annotaions;

import com.bloodlink.validators.WorkTimeCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // Аннотация применяется к классу
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WorkTimeCheckValidator.class) // Указываем валидатор
public @interface WorkTimeCheckAnnotation {
    String message() default "Организация должна открыться раньше, чем закрыться";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String minutesStart();

    String minutesEnd();

    String hoursStart();

    String hoursEnd();
}