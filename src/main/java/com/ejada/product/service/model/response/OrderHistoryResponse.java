package com.ejada.product.service.model.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
public class OrderHistoryResponse {

    @Schema(description = "Order Id", name = "OredreId")
    @JsonProperty("OredreId")
    private int orderId;
    @Schema(description = "Order Date", name = "OrderDate")
    @JsonProperty("OrderDate")
    private LocalDateTime orderDate;
    @Schema(description = "Order Status", name = "OrderStatus")
    @JsonProperty("OrderStatus")
    private String orderStatus;

    @Schema(description = "Total Cost", name = "TotalCost")
    @JsonProperty("TotalCost")
    private Double totalCost;

    @Schema(description = "Discount Amount", name = "DiscountAmount")
    @JsonProperty("DiscountAmount")
    private BigDecimal discountAmount;

    @Schema(description = "TotalAmountAfterDiscount", name = "TotalAmountAfterDiscount")
    @JsonProperty("TotalAmountAfterDiscount")
    private BigDecimal totalAmountAfterDiscount;

    @Schema(description = "Product Details", name = "ProductDetails")
    @JsonProperty("ProductDetails")
    private  List<ProductDetailsResponse> productDetails;
}
