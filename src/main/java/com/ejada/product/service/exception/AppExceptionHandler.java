package com.ejada.product.service.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Object> handleBusinessLogicException(BusinessException ex) {
        log.error("BusinessException: [{}]", ex.getMessage());
        ApiBusinessErrorResponse apiBusinessErrorResponse = ApiBusinessErrorResponse.builder()
                .httpStatus(ex.getHttpStatus().getReasonPhrase())
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getMessage())
                .traceId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(ex.getHttpStatus()).body(apiBusinessErrorResponse);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleBusinessLogicException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException: [{}]", ex.getBindingResult().getFieldErrors());
        List<String> fieldErrors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        ApiBusinessErrorResponse apiBusinessErrorResponse = ApiBusinessErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errorCode(ErrorCodeEnum.BAD_REQUEST.getCode())
                .errorMessage(fieldErrors.toString())
                .traceId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiBusinessErrorResponse);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleBusinessLogicException(ConstraintViolationException ex) {
        log.error("ConstraintViolationException: [{}]", ex.getMessage());
        ApiBusinessErrorResponse apiBusinessErrorResponse = ApiBusinessErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errorCode(ErrorCodeEnum.BAD_REQUEST.getCode())
                .errorMessage(ex.getMessage())
                .traceId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiBusinessErrorResponse);
    }

}
