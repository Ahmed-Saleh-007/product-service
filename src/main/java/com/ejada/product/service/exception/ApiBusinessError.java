package com.ejada.product.service.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiBusinessError {

    private String traceId;

    private String httpStatus;

    private String errorCode;

    private String errorMessage;

    private LocalDateTime timestamp;

}
