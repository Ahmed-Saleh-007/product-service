package com.ejada.product.service.model.mapper;

import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    List<ProductResponse> mapToProductResponse(List<Product> products);

    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "inStock", expression = "java(product.getStockQuantity() > 0)")
    ProductResponse mapToProductResponse(Product product);

}
