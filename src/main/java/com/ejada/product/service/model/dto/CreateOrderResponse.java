package com.ejada.product.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponse {
    private Integer orderId;
    private BigDecimal totalAmount;
    private String status;
}
