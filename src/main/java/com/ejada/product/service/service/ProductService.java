package com.ejada.product.service.service;

import com.ejada.product.service.model.dto.CreateProductRequest;
import com.ejada.product.service.model.dto.CreateProductResponse;
import com.ejada.product.service.model.dto.ProductEntityMapper;
import com.ejada.product.service.model.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    public List<Objects> getProducts() {
        log.info("Get Products");
        //throw new BusinessException("Dev Error", "001", HttpStatus.BAD_REQUEST);
        return List.of();
    }

    public CreateProductResponse createProduct(CreateProductRequest request) {
        Product productEntity = ProductEntityMapper.INSTANCE.mapToProductEntity(request);
        return CreateProductResponse.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .category(request.getCategory())
                .quantity(request.getQuantity())
                .build();
    }
}
