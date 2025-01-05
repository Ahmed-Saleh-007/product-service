package com.ejada.product.service.service;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.model.filter.ProductFilter;
import com.ejada.product.service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(PER_CLASS)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockitoBean
    private ProductRepository productRepository;

    @Test
    void testGetProductsSuccess() {
        Page<Product> mockPage = new PageImpl<>(List.of(
                Product.builder().id(1).name("Product 1").stockQuantity(1).build(),
                Product.builder().id(2).name("Product 2").stockQuantity(0).build()
        ));
        when(productRepository.findAllByCategoryAndPriceRange(any(), any()))
                .thenReturn(mockPage);
        assertDoesNotThrow(() -> productService.getProducts(
                ProductFilter.builder()
                        .pageIndex(0)
                        .pageSize(10)
                        .build()));
    }

    @Test
    void testGetProductsWithAscOrderSuccess() {
        Page<Product> mockPage = new PageImpl<>(List.of(
                Product.builder().id(1).name("Product 1").stockQuantity(1).build(),
                Product.builder().id(2).name("Product 2").stockQuantity(0).build()
        ));
        when(productRepository.findAllByCategoryAndPriceRange(any(), any()))
                .thenReturn(mockPage);
        assertDoesNotThrow(() -> productService.getProducts(
                ProductFilter.builder()
                        .pageIndex(0)
                        .pageSize(10)
                        .sortField("price")
                        .sortOrder("asc")
                        .build()));
    }

    @Test
    void testGetProductsWithDescOrderSuccess() {
        Page<Product> mockPage = new PageImpl<>(List.of(
                Product.builder().id(1).name("Product 1").stockQuantity(1).build(),
                Product.builder().id(2).name("Product 2").stockQuantity(0).build()
        ));
        when(productRepository.findAllByCategoryAndPriceRange(any(), any()))
                .thenReturn(mockPage);
        assertDoesNotThrow(() -> productService.getProducts(
                ProductFilter.builder()
                        .pageIndex(0)
                        .pageSize(10)
                        .sortField("price")
                        .sortOrder("desc")
                        .build()));
    }

    @Test
    void testGetProductsWithInvalidFilterFails() {
        Page<Product> mockPage = new PageImpl<>(List.of(
                Product.builder().id(1).name("Product 1").stockQuantity(1).build(),
                Product.builder().id(2).name("Product 2").stockQuantity(0).build()
        ));
        when(productRepository.findAllByCategoryAndPriceRange(any(), any()))
                .thenReturn(mockPage);
        assertThrows(BusinessException.class, () -> productService.getProducts(
                ProductFilter.builder()
                        .pageIndex(0)
                        .pageSize(10)
                        .minPrice(1000.0)
                        .maxPrice(100.0)
                        .build()));
    }

}
