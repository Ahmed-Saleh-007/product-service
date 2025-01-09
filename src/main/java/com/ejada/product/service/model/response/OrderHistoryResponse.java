package com.ejada.product.service.model.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Customer Id", name = "CustomerId")
    @JsonProperty("CustomerId")
    private int customerId;
    @Schema(description = "Customer Name", name = "CustomerName")
    @JsonProperty("CustomerName")
    private String customerName;
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
    @Schema(description = "Product Details", name = "ProductDetails")
    @JsonProperty("ProductDetails")
    private  List<ProductDetailsResponse> productDetails;
}
