package com.ejada.product.service.model.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {

    private List<Long> categoryIds;

    private Double minPrice;

    private Double maxPrice;

    private boolean isInStock;

    private int pageIndex;

    private int pageSize;

    private String sortOrder;

    private String sortField;

}
