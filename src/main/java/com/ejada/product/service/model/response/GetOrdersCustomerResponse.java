package com.ejada.product.service.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetOrdersCustomerResponse {
    @Schema(description = "CustomerID", name = "CustomerID")
    @JsonProperty("CustomerID")
    private Integer id;

    @Schema(description = "Customer Name", name = "CustomerName")
    @JsonProperty("CustomerName")
    private String customerName;

    @Schema(description = "Customer Email", name = "CustomerEmail")
    @JsonProperty("CustomerEmail")
    private String customerEmail;

    @Schema(description = "Customer Phone", name = "CustomerPhone")
    @JsonProperty("CustomerPhone")
    private String customerPhone;

    @Schema(description = "Customer Address", name = "CustomerAddress")
    @JsonProperty("CustomerAddress")
    private String customerAddress;

}
