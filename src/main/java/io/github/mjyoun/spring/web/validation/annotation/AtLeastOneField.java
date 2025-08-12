package io.github.mjyoun.spring.web.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import io.github.mjyoun.spring.web.validation.validator.AtLeastOneFieldValidator;

/**
 * 하나 이상의 값이 설정되어 있는지 확인하기 위한 annotation
 * 
 * @author MJ Youn
 * @since 2025. 08. 12.
 */
@Documented
@Constraint(validatedBy = AtLeastOneFieldValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneField {

    String message() default "최소 하나 이상의 파라미터가 존재해야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
