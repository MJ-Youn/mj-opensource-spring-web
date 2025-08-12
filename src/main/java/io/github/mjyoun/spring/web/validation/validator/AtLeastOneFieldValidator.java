package io.github.mjyoun.spring.web.validation.validator;

import java.lang.reflect.Field;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import io.github.mjyoun.spring.web.validation.annotation.AtLeastOneField;

/**
 * {@link AtLeastOneField} validator
 * 
 * @author MJ Youn
 * @since 2025. 08. 12.
 */
public class AtLeastOneFieldValidator implements ConstraintValidator<AtLeastOneField, Object> {

    /**
     * @see ConstraintValidator#isValid(Object, ConstraintValidatorContext)
     * 
     * @author MJ Youn
     * @since 2025. 08. 12.
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // value가 null이면, 이 유효성 검사를 통과하도록 합니다.
        // 일반적으로 @NotNull 같은 다른 어노테이션이 null을 처리합니다.
        if (value == null) {
            return true;
        }

        // 리플렉션을 사용하여 DTO 객체의 모든 필드를 가져옵니다.
        for (Field field : value.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true); // private 필드에 접근 허용
                // 필드의 값이 null이 아니면, 최소 하나가 존재하므로 true 반환
                if (Objects.nonNull(field.get(value))) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                // 접근 예외가 발생하면 무시하거나 적절히 처리
            }
        }
        
        // 모든 필드가 null이면 false 반환
        return false;
    }

}
