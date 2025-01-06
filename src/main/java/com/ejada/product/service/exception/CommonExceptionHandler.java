package com.ejada.product.service.exception;

import org.springframework.http.HttpStatus;

public class CommonExceptionHandler {
    private CommonExceptionHandler() {
    }
    public static BusinessException handleBadRequestException(String message)
    {
        return BusinessException.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .errorCode(ErrorCodeEnum.BAD_REQUEST.getCode())
                .message(message)
                .build();
    }
}
