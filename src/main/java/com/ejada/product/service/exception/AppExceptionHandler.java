package com.ejada.product.service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Object> handleBusinessLogicException(BusinessException ex) {
        log.error("Exception: [{}]", ex.getMessage());
        ApiBusinessErrorResponse apiBusinessErrorResponse = ApiBusinessErrorResponse.builder()
                .httpStatus(ex.getHttpStatus().getReasonPhrase())
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getMessage())
                .traceId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(ex.getHttpStatus()).body(apiBusinessErrorResponse);
    }

}
