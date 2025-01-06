package com.ejada.product.service.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.ejada.product.service.util.Constants.POSITIVE_QUANTITY;
import static com.ejada.product.service.util.Constants.PRODUCT_QUANTITY_REQUIRED;
import static com.ejada.product.service.util.Constants.PRODUCT_REQUIRED;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDto {
    @NotNull(message = PRODUCT_REQUIRED)
    private Integer productId;

    @NotNull(message = PRODUCT_QUANTITY_REQUIRED)
    @Positive(message = POSITIVE_QUANTITY)
    private Integer quantity;
}
