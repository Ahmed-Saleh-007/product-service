package com.ejada.product.service.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class OrderProductRequest {
    @NotNull(message = PRODUCT_REQUIRED)
    @JsonProperty("ProductId")
    private Integer productId;

    @NotNull(message = PRODUCT_QUANTITY_REQUIRED)
    @Positive(message = POSITIVE_QUANTITY)
    @JsonProperty("Quantity")
    private Integer quantity;
}
