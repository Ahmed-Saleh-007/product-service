package com.ejada.product.service.model.dto;

import com.ejada.product.service.model.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.ejada.product.service.util.Constants.CATEGORY_ID_IS_REQUIRED_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.DESCRIPTION_MAX_SIZE_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.PRODUCT_NAME_IS_REQUIRED_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.PRODUCT_NAME_MAX_SIZE_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.PRODUCT_PRICE_IS_REQUIRED_ERROR_MESSAGE;

import static com.ejada.product.service.util.Constants.PRODUCT_PRICE_MIN_VALUE_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.PRODUCT_QUANTITY_IS_REQUIRED_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.PRODUCT_QUANTITY_MIN_VALUE_ERROR_MESSAGE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {
    @NotBlank(message = PRODUCT_NAME_IS_REQUIRED_ERROR_MESSAGE)
    @Size(max = 255, message = PRODUCT_NAME_MAX_SIZE_ERROR_MESSAGE)
    @JsonProperty("Name")
    private String name;

    @NotNull(message = PRODUCT_PRICE_IS_REQUIRED_ERROR_MESSAGE)
    @JsonProperty("Price")
    @DecimalMin(value = "0.0", inclusive = true, message = PRODUCT_PRICE_MIN_VALUE_ERROR_MESSAGE)
    private BigDecimal price;

    @JsonProperty("Description")
    @Size(max = 2000, message = DESCRIPTION_MAX_SIZE_ERROR_MESSAGE)
    private String description;

    @NotNull(message = PRODUCT_QUANTITY_IS_REQUIRED_ERROR_MESSAGE)
    @JsonProperty("Quantity")
    @Min(value = 0, message = PRODUCT_QUANTITY_MIN_VALUE_ERROR_MESSAGE)
    private Integer quantity;

    @NotNull(message = CATEGORY_ID_IS_REQUIRED_ERROR_MESSAGE)
    @JsonProperty("CategoryId")
    private Integer categoryId;

}