package com.ejada.product.service.model.mapper;

import com.ejada.product.service.model.request.CreateProductRequest;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    List<ProductResponse> mapToProductResponse(List<Product> products);

    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "inStock", expression = "java(product.getStockQuantity() > 0)")
    ProductResponse mapToProductResponse(Product product);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "stockQuantity", source = "quantity")
    Product mapToProductEntity(CreateProductRequest createProductRequest);

}
