package com.ejada.product.service.repository;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.model.filter.ProductFilter;
import com.ejada.product.service.repository.facade.ProductRepositoryFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.ejada.product.service.util.Constants.DATABASE_GENERAL_ERROR_MESSAGE;
import static com.ejada.product.service.utils.TestUtils.buildProduct;
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
    private ProductRepositoryFacade productRepositoryFacade;

    @MockitoBean
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setup() {
        product = buildProduct();
    }

    @Test
    void testFindAllProductsByCategoryAndPriceRangeSuccess() {
        when(productRepository.findAllByCategoryAndPriceRange(any(), any()))
                .thenReturn(Page.empty());
        assertDoesNotThrow(() -> productRepositoryFacade.findAllByCategoryAndPriceRange(
                ProductFilter.builder().build(),
                PageRequest.of(0, 10)));
    }

    @Test
    void testFindAllProductsByCategoryAndPriceRangeFailsWithSqlException() {
        doAnswer(
                invocation -> {
                    throw new SQLException("");
                })
                .when(productRepository)
                .findAllByCategoryAndPriceRange(any(), any());
        assertThrows(BusinessException.class, () ->
                productRepositoryFacade.findAllByCategoryAndPriceRange(
                        ProductFilter.builder().build(),
                        PageRequest.of(0, 10)));
    }

    @Test
    void testFindProductByIdSuccess() {
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        assertDoesNotThrow(() -> productRepositoryFacade.findProductById(1));
    }

    @Test
    void testFindProductByIdFailsWithSqlException() {
        doAnswer(
                invocation -> {
                    throw new SQLException("");
                })
                .when(productRepository)
                .findById(any());
        assertThrows(BusinessException.class, () -> productRepositoryFacade.findProductById(1));
    }

    @Test
    void testFindProductByNameSuccess() {
        when(productRepository.findByName(any())).thenReturn(Optional.of(product));
        assertDoesNotThrow(() -> productRepositoryFacade.findByName(""));
    }

    @Test
    void testFindProductByNameFailsWithSqlException() {
        doAnswer(
                invocation -> {
                    throw new SQLException("");
                })
                .when(productRepository)
                .findByName(any());
        assertThrows(BusinessException.class, () -> productRepositoryFacade.findByName(""));
    }

    @Test
    void testSaveProductSuccess() {
        when(productRepository.save(any())).thenReturn(product);
        assertDoesNotThrow(() -> productRepositoryFacade.save(product));
    }

    @Test
    void testSaveProductFailsWithSqlException() {
        doAnswer(
                invocation -> {
                    throw new SQLException("");
                })
                .when(productRepository)
                .save(any());
        assertThrows(BusinessException.class, () -> productRepositoryFacade.save(product));
    }


    @Test
    void findAllById_success() {
        List<Integer> productIds = List.of(1, 2, 3);
        List<Product> products = List.of(product);

        Mockito.when(productRepository.findAllByIdExcludingDeleted(productIds)).thenReturn(products);

        List<Product> result = productRepositoryFacade.findAllById(productIds);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(product.getId(), result.get(0).getId());

        Mockito.verify(productRepository).findAllByIdExcludingDeleted(productIds);
    }

    @Test
    void findAllById_exception() {
        List<Integer> productIds = List.of(1, 2, 3);
        Mockito.when(productRepository.findAllByIdExcludingDeleted(productIds))
                .thenThrow(new RuntimeException(DATABASE_GENERAL_ERROR_MESSAGE));

        Assertions.assertThrows(BusinessException.class, () -> {
            productRepositoryFacade.findAllById(productIds);
        });

        Mockito.verify(productRepository).findAllByIdExcludingDeleted(productIds);
    }

    @Test
    void updateProduct_success() {
        Mockito.when(productRepository.save(product)).thenReturn(product);
        product.setStockQuantity(9);
        Assertions.assertDoesNotThrow(() -> productRepositoryFacade.updateProduct(product));
        Assertions.assertEquals(9, product.getStockQuantity());
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void updateProduct_exception() {
        Mockito.when(productRepository.save(product)).thenThrow(new RuntimeException(DATABASE_GENERAL_ERROR_MESSAGE));
        Assertions.assertThrows(BusinessException.class, () -> {
            productRepositoryFacade.updateProduct(product);
        });
        Mockito.verify(productRepository).save(product);
    }

}
