package com.system.transaction_service.validation.interfaces;

import com.system.transaction_service.util.Constant;
import com.system.transaction_service.validation.NapasCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NapasCodeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface NapasCodeConstraint {

    String message() default "{" + Constant.INVALID_NAPAS_CODE + "}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
