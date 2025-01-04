package com.ejada.product.service.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithPagingResponse {

    @Schema(description = "List of products", name = "Products")
    @JsonProperty("Products")
    private List<ProductResponse> products;

    @Schema(description = "Total count of products", name = "TotalCount")
    @JsonProperty("TotalCount")
    private Long totalCount;

    @Schema(description = "Page count of products", name = "PageCount")
    @JsonProperty("PageCount")
    private int pageCount;

}
