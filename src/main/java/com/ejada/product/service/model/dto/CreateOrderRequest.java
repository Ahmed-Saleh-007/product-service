package com.ejada.product.service.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.ejada.product.service.util.Constants.CUSTOMER_REQUIRED;
import static com.ejada.product.service.util.Constants.MIN_PRODUCTS_IN_ORDER;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    @NotNull(message = CUSTOMER_REQUIRED)
    private Integer customerId;

    @NotNull
    @Size(min = 1, message = MIN_PRODUCTS_IN_ORDER)
    @Valid
    private List<OrderProductDto> products;

}
