package com.ejada.product.service.model.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.ejada.product.service.util.Constants.POSITIVE_QUANTITY;
import static com.ejada.product.service.util.Constants.PRODUCT_NAME_IS_REQUIRED_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.PRODUCT_PRICE_IS_REQUIRED_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.PRODUCT_PRICE_MIN_VALUE_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.PRODUCT_QUANTITY_REQUIRED;
import static com.ejada.product.service.util.Constants.PRODUCT_REQUIRED;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOrdersOrderProductDto {
    @NotNull(message = PRODUCT_REQUIRED)
    private Integer productId;

    @NotNull(message = PRODUCT_QUANTITY_REQUIRED)
    @Positive(message = POSITIVE_QUANTITY)
    private Integer quantity;

    @NotNull(message = PRODUCT_PRICE_IS_REQUIRED_ERROR_MESSAGE)
    @Positive(message = PRODUCT_PRICE_MIN_VALUE_ERROR_MESSAGE)
    private BigDecimal price;

    @NotNull(message = PRODUCT_NAME_IS_REQUIRED_ERROR_MESSAGE)
    private String productName;
}