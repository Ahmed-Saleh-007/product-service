package com.ejada.product.service.model.response;

import com.ejada.product.service.model.entity.Customer;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOrdersResponse {
    @Schema(description = "Order ID", name = "OrderID")
    @JsonProperty("ID")
    private Integer id;

    @Schema(description = "Customer", name = "Customer")
    @JsonProperty("Customer")
    private GetOrdersCustomerResponse customer;

    @Schema(description = "Total Amount", name = "TotalAmount")
    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;

    @Schema(description = "Discount Amount", name = "DiscountAmount")
    @JsonProperty("DiscountAmount")
    private BigDecimal discountAmount;

    @Schema(description = "Total Amount After Discount", name = "TotalAmountAfterDiscount")
    @JsonProperty("TotalAmountAfterDiscount")
    private BigDecimal totalAmountAfterDiscount;

    @Schema(description = "Status", name = "Status")
    @JsonProperty("Status")
    private String status;

    @Schema(description = "Created At", name = "CreatedAt")
    @JsonProperty("CreatedAt")
    private LocalDateTime createdAt;

    @Schema(description = "Order Products", name = "OrderProducts")
    @JsonProperty("OrderProducts")
    private List<GetOrdersOrderProductResponse> orderProducts;
}