package com.ejada.product.service.model.dto;

import com.ejada.product.service.model.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CreateProductRequest {
    @NotBlank
    @JsonProperty("Name")
    private String name;
    @NotNull
    @JsonProperty("Price")
    private BigDecimal price;
    @JsonProperty("Description")
    private String description;
    @NotNull
    @JsonProperty("Quantity")
    private Integer quantity;
    @NotNull
    @JsonProperty("CategoryId")
    private Integer categoryId;

}