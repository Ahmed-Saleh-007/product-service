package com.ejada.product.service.service;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.model.entity.Customer;
import com.ejada.product.service.model.entity.Order;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.model.entity.Promotion;
import com.ejada.product.service.model.request.CreateOrderRequest;
import com.ejada.product.service.model.response.CreateOrderResponse;
import com.ejada.product.service.repository.facade.CustomerRepositoryFacade;
import com.ejada.product.service.repository.facade.OrderRepositoryFacade;
import com.ejada.product.service.repository.facade.ProductRepositoryFacade;
import com.ejada.product.service.repository.facade.PromotionRepositoryFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ejada.product.service.utils.TestUtils.buildAmountPromotion;
import static com.ejada.product.service.utils.TestUtils.buildCustomer;
import static com.ejada.product.service.utils.TestUtils.buildOrderRequest;
import static com.ejada.product.service.utils.TestUtils.buildPrecentagePromotion;
import static com.ejada.product.service.utils.TestUtils.buildProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private CustomerRepositoryFacade customerRepositoryFacade;

    @Mock
    private ProductRepositoryFacade productRepositoryFacade;

    @Mock
    private OrderRepositoryFacade orderRepositoryFacade;

    @Mock
    private PromotionRepositoryFacade promotionRepositoryFacade;

    @InjectMocks
    private OrderService orderService;

    // Test data setup
    private CreateOrderRequest createOrderRequest;
    private Customer customer;
    private Product product;
    private Promotion precentagePromotion;
    private Promotion amountPromotion;

    @BeforeEach
    void setup() {
        customer = buildCustomer();
        product = buildProduct();
        precentagePromotion = buildPrecentagePromotion();
        amountPromotion = buildAmountPromotion();
        createOrderRequest = buildOrderRequest();
    }

    @Test
    void createOrder_success() {
        when(customerRepositoryFacade.findById(1)).thenReturn(Optional.of(customer));
        when(productRepositoryFacade.findAllById(List.of(1))).thenReturn(List.of(product));
        when(orderRepositoryFacade.createOrder(Mockito.any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1);
            return order;
        });

        CreateOrderResponse response = orderService.createOrder(createOrderRequest);

        assertNotNull(response);
        assertEquals(1, response.getOrderId());
        assertEquals(new BigDecimal("59.98"), response.getTotalAmount());

        verify(customerRepositoryFacade).findById(1);
        verify(productRepositoryFacade).findAllById(List.of(1));
        verify(orderRepositoryFacade).createOrder(Mockito.any(Order.class));
    }

    @Test
    void createOrder_customerNotFound() {
        when(customerRepositoryFacade.findById(1)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> orderService.createOrder(createOrderRequest));
        verify(customerRepositoryFacade).findById(1);
        verifyNoInteractions(productRepositoryFacade, orderRepositoryFacade);
    }

    @Test
    void createOrder_productNotFound() {
        when(customerRepositoryFacade.findById(1)).thenReturn(Optional.of(customer));
        when(productRepositoryFacade.findAllById(List.of(1))).thenReturn(Collections.emptyList());

        assertThrows(BusinessException.class, () -> orderService.createOrder(createOrderRequest));
        verify(productRepositoryFacade).findAllById(List.of(1));
        verifyNoInteractions(orderRepositoryFacade);
    }

    @Test
    void createOrder_insufficientStock() {
        product.setStockQuantity(1);

        when(customerRepositoryFacade.findById(1)).thenReturn(Optional.of(customer));
        when(productRepositoryFacade.findAllById(List.of(1))).thenReturn(List.of(product));

        assertThrows(BusinessException.class, () -> orderService.createOrder(createOrderRequest));
        verify(productRepositoryFacade).findAllById(List.of(1));
        verifyNoInteractions(orderRepositoryFacade);
    }

    @Test
    void createOrderWithNotValidPromotionFailed() {
        createOrderRequest.setPromotionCode("PROMO2025");
        when(customerRepositoryFacade.findById(1)).thenReturn(Optional.of(customer));
        when(productRepositoryFacade.findAllById(List.of(1))).thenReturn(List.of(product));
        when(promotionRepositoryFacade.findActiveByCode(any())).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> orderService.createOrder(createOrderRequest));
    }

    @Test
    void createOrderWithPercentagePromotionSuccess() {
        createOrderRequest.setPromotionCode("PROMO2025");
        when(customerRepositoryFacade.findById(1)).thenReturn(Optional.of(customer));
        when(productRepositoryFacade.findAllById(List.of(1))).thenReturn(List.of(product));
        when(promotionRepositoryFacade.findActiveByCode(any())).thenReturn(Optional.of(precentagePromotion));
        when(orderRepositoryFacade.createOrder(Mockito.any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1);
            return order;
        });
        CreateOrderResponse response = orderService.createOrder(createOrderRequest);
        assertNotNull(response);
        assertEquals(1, response.getOrderId());
    }

    @Test
    void createOrderWithAmountPromotionSuccess() {
        createOrderRequest.setPromotionCode("PROMO500");
        when(customerRepositoryFacade.findById(1)).thenReturn(Optional.of(customer));
        when(productRepositoryFacade.findAllById(List.of(1))).thenReturn(List.of(product));
        when(promotionRepositoryFacade.findActiveByCode(any())).thenReturn(Optional.of(amountPromotion));
        when(orderRepositoryFacade.createOrder(Mockito.any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1);
            return order;
        });
        CreateOrderResponse response = orderService.createOrder(createOrderRequest);
        assertNotNull(response);
        assertEquals(1, response.getOrderId());
    }

}
