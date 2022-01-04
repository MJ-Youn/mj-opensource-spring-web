package io.github.mjyoun.spring.web.error;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.mjyoun.core.data.Result;

/**
 * 에러 핸들 controller
 * 
 * @author MJ Youn
 * @since 2022. 01. 04.
 */
@RestControllerAdvice
public class CustomErrorController {

    /**
     * validate 오류시 동작하는 화면
     * 
     * @param e
     *            exception 정보
     * @return {@link ResponseEntity}
     * 
     * @author MJ Youn
     * @since 2022. 01. 04.
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class, ConstraintViolationException.class })
    public ResponseEntity<Result<String>> argumentCheck(Exception e) {
        Result<String> result = null;

        if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            String[] violateMsgs = !bindingResult.hasErrors() ? new String[0] //
                    : bindingResult.getAllErrors() //
                            .stream() //
                            .filter(error -> error instanceof FieldError) //
                            .map(ObjectError::getDefaultMessage) //
                            .toArray(String[]::new);

            result = Result.error(String.join(",", violateMsgs));
        } else if (e instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) e).getConstraintViolations();

            String[] violateMsgs = constraintViolations //
                    .stream() //
                    .map(ConstraintViolation::getMessage) //
                    .toArray(String[]::new);

            result = Result.error(String.join(",", violateMsgs));
        }

        return new ResponseEntity<Result<String>>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * 모든 exception 발생시 동작하는 함수
     * 
     * @param e
     *            exception 정보
     * @return {@link ResponseEntity}
     * 
     * @author MJ Youn
     * @since 2022. 01. 04.
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Result<String>> error(Exception e) {
        Result<String> result = Result.error(e.getMessage());

        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
