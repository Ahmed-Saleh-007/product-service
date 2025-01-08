package com.ejada.product.service.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("OrderId")
    private Integer orderId;
    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;
    @JsonProperty("Status")
    private String status;
}
