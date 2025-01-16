package com.system.common_library.validation;

import com.system.common_library.enums.AccountType;
import com.system.common_library.util.Constant;
import com.system.common_library.validation.interfaces.AccountTypeConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;

@RequiredArgsConstructor
public class AccountTypeValidator implements ConstraintValidator<AccountTypeConstraint, Object> {

    private final MessageSource messageSource;

    @Override
    public void initialize(AccountTypeConstraint constraintAnnotation) {

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {

        try {

            if (value == null) {

                constraintValidatorContext
                        .buildConstraintViolationWithTemplate(messageSource.getMessage
                                (Constant.ACCOUNT_TYPE_REQUIRE, null, LocaleContextHolder.getLocale()))
                        .addConstraintViolation();

                return false;
            } else if (!Arrays.asList(AccountType.values()).contains((AccountType) value)) {

                constraintValidatorContext
                        .buildConstraintViolationWithTemplate(messageSource.getMessage
                                (Constant.INVALID_ACCOUNT_TYPE, null, LocaleContextHolder.getLocale()))
                        .addConstraintViolation();

                return false;
            }

            return true;
        } catch (Exception e) {

            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(messageSource.getMessage
                            (Constant.INVALID_ACCOUNT_TYPE, null, LocaleContextHolder.getLocale()))
                    .addConstraintViolation();

            return false;
        }
    }
}
