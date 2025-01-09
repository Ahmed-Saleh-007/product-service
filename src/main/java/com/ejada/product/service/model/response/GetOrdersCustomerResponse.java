package com.ejada.product.service.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetOrdersCustomerResponse {
    @JsonProperty("CustomerID")
    private Integer id;

    @JsonProperty("CustomerName")
    private String customerName;

    @JsonProperty("CustomerEmail")
    private String customerEmail;

    @JsonProperty("CustomerPhone")
    private String customerPhone;

    @JsonProperty("CustomerAddress")
    private String customerAddress;

}
