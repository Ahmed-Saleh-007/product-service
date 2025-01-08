package com.ejada.product.service.model.dto;

import com.ejada.product.service.model.entity.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersResponse {
    @Schema(description = "List of orders", name = "Orders")
    @JsonProperty("Orders")
    private List<GetOrdersResponse> orders;

    @Schema(description = "Total count of products", name = "TotalCount")
    @JsonProperty("TotalCount")
    private Long totalCount;

    @Schema(description = "Page count of products", name = "PageCount")
    @JsonProperty("PageCount")
    private int pageCount;
}
