package io.github.mjyoun.spring.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * method 호출시 시간을 기록하기 위한 stop watch의 task 정보
 * 
 * @author MJ Youn
 * @since 2024. 05. 09.
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StopWatchRecord {

    @AliasFor("taskName")
    String value() default "";

    /**
     * Task 이름
     */
    @AliasFor("value")
    String taskName() default "";
    
}
