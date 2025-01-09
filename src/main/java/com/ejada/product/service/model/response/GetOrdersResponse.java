package com.ejada.product.service.model.response;

import com.ejada.product.service.model.entity.Customer;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("Customer")
    private GetOrdersCustomerResponse customer;

    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("DiscountAmount")
    private BigDecimal discountAmount;

    @JsonProperty("TotalAmountAfterDiscount")
    private BigDecimal totalAmountAfterDiscount;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("CreatedAt")
    private LocalDateTime createdAt;

    @JsonProperty("OrderProducts")
    private List<GetOrdersOrderProductResponse> orderProducts;
}