package com.ejada.product.service.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Customer Id", name = "CustomerId")
    @NotNull(message = CUSTOMER_REQUIRED)
    @JsonProperty("CustomerId")
    private Integer customerId;

    @Schema(description = "List of order products", name = "Products")
    @NotNull
    @Size(min = 1, message = MIN_PRODUCTS_IN_ORDER)
    @Valid
    @JsonProperty("Products")
    private List<OrderProductRequest> products;

    @Schema(description = "Promotion code", name = "PromotionCode")
    @JsonProperty("PromotionCode")
    private String promotionCode;

}
