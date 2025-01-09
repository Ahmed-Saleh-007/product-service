package com.ejada.product.service.service;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.model.entity.Category;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.model.filter.ProductFilter;
import com.ejada.product.service.model.mapper.ProductMapper;
import com.ejada.product.service.model.request.CreateProductRequest;
import com.ejada.product.service.model.response.CreateProductResponse;
import com.ejada.product.service.repository.facade.CategoryRepositoryFacade;
import com.ejada.product.service.repository.facade.ProductRepositoryFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static com.ejada.product.service.util.Constants.PRODUCT_ALREADY_EXISTS_ERROR_MESSAGE;
import static com.ejada.product.service.utils.TestUtils.buildCreateProductRequest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(PER_CLASS)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockitoBean
    private ProductRepositoryFacade productRepositoryFacade;

    @MockitoBean
    private CategoryRepositoryFacade categoryRepositoryFacade;

    @Test
    void testGetProductsSuccess() {
        Page<Product> mockPage = new PageImpl<>(List.of(
                Product.builder().id(1).name("Product 1").stockQuantity(1).build(),
                Product.builder().id(2).name("Product 2").stockQuantity(0).build()
        ));
        when(productRepositoryFacade.findAllByCategoryAndPriceRange(any(), any()))
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
        when(productRepositoryFacade.findAllByCategoryAndPriceRange(any(), any()))
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
        when(productRepositoryFacade.findAllByCategoryAndPriceRange(any(), any()))
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
        assertThrows(BusinessException.class, () -> productService.getProducts(
                ProductFilter.builder()
                        .pageIndex(0)
                        .pageSize(10)
                        .minPrice(1000.0)
                        .maxPrice(100.0)
                        .build()));
    }

    @Test
    void testCreateProductSuccess() {
        CreateProductRequest request = buildCreateProductRequest();

        Product mappedProduct = new Product();
        mappedProduct.setName(request.getName());
        mappedProduct.setPrice(request.getPrice());
        mappedProduct.setDescription(request.getDescription());
        mappedProduct.setStockQuantity(request.getQuantity());

        Category category = new Category();
        category.setId(1);
        category.setName("Test Category");

        when(productRepositoryFacade.findByName(request.getName())).thenReturn(Optional.empty());
        when(categoryRepositoryFacade.findById(request.getCategoryId())).thenReturn(Optional.of(category));
        doNothing().when(productRepositoryFacade).save(any(Product.class));

        CreateProductResponse response = productService.createProduct(request);
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getPrice(), response.getPrice());
        assertEquals(request.getDescription(), response.getDescription());
        assertEquals(category, response.getCategory());
        assertEquals(request.getQuantity(), response.getQuantity());
    }

    @Test
    void testCreateProductFailureWhenProductNameExists() {
        CreateProductRequest request = buildCreateProductRequest();

        Product existingProduct = new Product();
        existingProduct.setName(request.getName());

        when(productRepositoryFacade.findByName(request.getName())).thenReturn(Optional.of(existingProduct));

        Exception exception = assertThrows(
                BusinessException.class,
                () -> productService.createProduct(request)
        );

        assertEquals(PRODUCT_ALREADY_EXISTS_ERROR_MESSAGE, exception.getMessage());
    }


}
