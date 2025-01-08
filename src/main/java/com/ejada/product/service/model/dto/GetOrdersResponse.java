package com.ejada.product.service.model.dto;

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
    private Customer customer;

    @JsonProperty("Total amount")
    private BigDecimal totalAmount;

    @JsonProperty("Discount amount")
    private BigDecimal discountAmount;

    @JsonProperty("Total amount after discount")
    private BigDecimal totalAmountAfterDiscount;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Created at")
    private LocalDateTime createdAt;

    @JsonProperty("Order Products")
    private List<GetOrdersOrderProductDto> orderProducts;
}
