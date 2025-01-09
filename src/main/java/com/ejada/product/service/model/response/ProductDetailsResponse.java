package com.ejada.product.service.model.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
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
public class ProductDetailsResponse {
    @Schema(description = "Product Name", name = "ProductName")
    @JsonProperty("ProductName")
    private String productName;
    @Schema(description = "Product Quantity", name = "Quantity")
    @JsonProperty("Quantity")
    private Integer quantity;
    @Schema(description = "Product Price", name = "Price")
    @JsonProperty("Price")
    private BigDecimal price;
    @Schema(description = "Total Cost Per Product Quantity", name = "Subtotal")
    @JsonProperty("Subtotal")
    private BigDecimal subtotal;
    @Schema(description = "Category Name", name = "CategoryName")
    @JsonProperty("CategoryName")
    private String categoryName;
}
