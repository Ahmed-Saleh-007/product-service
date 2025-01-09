package com.ejada.product.service.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponse {

    @Schema(description = "Order Id", name = "OrderId")
    @JsonProperty("OrderId")
    private Integer orderId;

    @Schema(description = "Total Amount", name = "TotalAmount")
    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;

    @Schema(description = "Discount Amount", name = "DiscountAmount")
    @JsonProperty("DiscountAmount")
    private BigDecimal discountAmount;

    @Schema(description = "Total Amount After Discount", name = "TotalAmountAfterDiscount")
    @JsonProperty("TotalAmountAfterDiscount")
    private BigDecimal totalAmountAfterDiscount;

    @Schema(description = "Order Status", name = "Status")
    @JsonProperty("Status")
    private String status;

}
