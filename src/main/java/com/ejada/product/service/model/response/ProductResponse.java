package com.ejada.product.service.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    @Schema(description = "Product Id", name = "Id")
    @JsonProperty("Id")
    private Long id;

    @Schema(description = "Product Name", name = "Name")
    @JsonProperty("Name")
    private String name;

    @Schema(description = "Product Description", name = "Description")
    @JsonProperty("Description")
    private String description;

    @Schema(description = "Product Category", name = "Category")
    @JsonProperty("Category")
    private String category;

    @Schema(description = "Product Price", name = "Price")
    @JsonProperty("Price")
    private Double price;

    @Schema(description = "Product Stock Quantity", name = "StockQuantity")
    @JsonProperty("StockQuantity")
    private Integer stockQuantity;

    @Schema(description = "Product Availability", name = "InStock")
    @JsonProperty("InStock")
    private boolean inStock;

}
