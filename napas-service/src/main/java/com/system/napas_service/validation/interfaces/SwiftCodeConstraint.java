package com.system.napas_service.validation.interfaces;

import com.system.napas_service.util.Constant;
import com.system.napas_service.validation.SwiftCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SwiftCodeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface SwiftCodeConstraint {

    String message() default "{" + Constant.INVALID_SWIFT_CODE + "}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
