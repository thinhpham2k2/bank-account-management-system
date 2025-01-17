package com.system.napas_service.validation.interfaces;

import com.system.napas_service.util.Constant;
import com.system.napas_service.validation.FileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface FileConstraint {

    String message() default "{" + Constant.INVALID_IMAGE_FILE + "}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
