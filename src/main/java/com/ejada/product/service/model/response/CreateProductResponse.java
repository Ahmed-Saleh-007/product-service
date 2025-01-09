package com.ejada.product.service.model.response;

import com.ejada.product.service.model.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductResponse {
    @NotBlank
    @Schema(description = "Product Name", name = "Name")
    @JsonProperty("Name")
    private String name;

    @NotNull
    @Schema(description = "Product Price", name = "Price")
    @JsonProperty("Price")
    private BigDecimal price;

    @Schema(description = "Product Description", name = "Description")
    @JsonProperty("Description")
    private String description;

    @NotNull
    @Schema(description = "Product Quantity", name = "Quantity")
    @JsonProperty("Quantity")
    private Integer quantity;

    @NotNull
    @Schema(description = "Product Category", name = "Category")
    @JsonProperty("Category")
    private Category category;
}