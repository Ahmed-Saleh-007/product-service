package com.ejada.product.service.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @Size(min = 1, message = "At least one product must be included in the order")
    private List<OrderProductDto> products;

}
