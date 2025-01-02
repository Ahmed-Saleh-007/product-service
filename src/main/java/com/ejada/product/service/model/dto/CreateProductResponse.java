package com.ejada.product.service.model.dto;

import com.ejada.product.service.model.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Price")
    private BigDecimal price;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Quantity")
    private Integer quantity;
    @JsonProperty("Category")
    private Category category;
}
