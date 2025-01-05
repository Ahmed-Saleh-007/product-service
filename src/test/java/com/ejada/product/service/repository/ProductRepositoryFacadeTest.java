package com.ejada.product.service.repository;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.model.filter.ProductFilter;
import com.ejada.product.service.repository.facade.ProductRepositoryFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(PER_CLASS)
class ProductRepositoryFacadeTest {

    @Autowired
    private ProductRepositoryFacade ProductRepositoryFacade;

    @MockitoBean
    private ProductRepository productRepository;

    @Test
    void testGetProductsSuccess() {
        when(productRepository.findAllByCategoryAndPriceRange(any(), any()))
                .thenReturn(Page.empty());
        assertDoesNotThrow(() -> ProductRepositoryFacade.findAllByCategoryAndPriceRange(
                ProductFilter.builder().build(),
                PageRequest.of(0, 10)));
    }

    @Test
    void testGetProductsFailsWithSqlException() {
        doAnswer(
                invocation -> {
                    throw new SQLException("");
                })
                .when(productRepository)
                .findAllByCategoryAndPriceRange(any(), any());
        assertThrows(BusinessException.class, () ->
                ProductRepositoryFacade.findAllByCategoryAndPriceRange(
                        ProductFilter.builder().build(),
                        PageRequest.of(0, 10)));
    }

}
