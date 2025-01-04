package com.ejada.product.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {

    INTERNAL_SERVER_ERROR("0001", "PRODUCT.INTERNAL_SERVER_ERROR"),
    BAD_REQUEST("0002", "PRODUCT.BAD_REQUEST"),
    UNAUTHORIZED("0003", "PRODUCT.UNAUTHORIZED"),
    FORBIDDEN("0004", "PRODUCT.FORBIDDEN"),
    NOT_FOUND("0005", "PRODUCT.NOT_FOUND");

    private final String id;

    private final String code;

}
