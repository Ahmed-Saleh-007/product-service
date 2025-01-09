package com.ejada.product.service.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Product Name", name = "ProductName")
    @JsonProperty("Name")
    private String name;

    @NotNull(message = PRODUCT_PRICE_IS_REQUIRED_ERROR_MESSAGE)
    @DecimalMin(value = "0.0", inclusive = true, message = PRODUCT_PRICE_MIN_VALUE_ERROR_MESSAGE)
    @Schema(description = "Product Price", name = "ProductPrice")
    @JsonProperty("Price")
    private BigDecimal price;

    @Size(max = 2000, message = DESCRIPTION_MAX_SIZE_ERROR_MESSAGE)
    @Schema(description = "Product Description", name = "ProductDescription")
    @JsonProperty("Description")
    private String description;

    @NotNull(message = PRODUCT_QUANTITY_IS_REQUIRED_ERROR_MESSAGE)
    @Min(value = 0, message = PRODUCT_QUANTITY_MIN_VALUE_ERROR_MESSAGE)
    @Schema(description = "Product Quantity", name = "ProductQuantity")
    @JsonProperty("Quantity")
    private Integer quantity;

    @NotNull(message = CATEGORY_ID_IS_REQUIRED_ERROR_MESSAGE)
    @Schema(description = "Category Id", name = "CategoryId")
    @JsonProperty("CategoryId")
    private Integer categoryId;

}