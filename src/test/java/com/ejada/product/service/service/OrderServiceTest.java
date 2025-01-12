package com.ejada.product.service.service;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.model.entity.Customer;
import com.ejada.product.service.model.entity.Order;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.model.entity.Promotion;
import com.ejada.product.service.model.filter.OrderFilter;
import com.ejada.product.service.model.request.CreateOrderRequest;
import com.ejada.product.service.model.response.CreateOrderResponse;
import com.ejada.product.service.model.response.OrdersResponse;
import com.ejada.product.service.repository.OrderRepository;
import com.ejada.product.service.repository.facade.CustomerRepositoryFacade;
import com.ejada.product.service.repository.facade.OrderRepositoryFacade;
import com.ejada.product.service.repository.facade.ProductRepositoryFacade;
import com.ejada.product.service.repository.facade.PromotionRepositoryFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ejada.product.service.utils.TestUtils.buildAmountPromotion;
import static com.ejada.product.service.utils.TestUtils.buildCustomer;
import static com.ejada.product.service.utils.TestUtils.buildOrderFilter;
import static com.ejada.product.service.utils.TestUtils.buildOrderRequest;
import static com.ejada.product.service.utils.TestUtils.buildPrecentagePromotion;
import static com.ejada.product.service.utils.TestUtils.buildProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
@SpringBootTest
@TestInstance(PER_CLASS)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockitoBean
    private CustomerRepositoryFacade customerRepositoryFacade;

    @MockitoBean
    private ProductRepositoryFacade productRepositoryFacade;

    @MockitoBean
    private OrderRepositoryFacade orderRepositoryFacade;

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private PromotionRepositoryFacade promotionRepositoryFacade;

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

    @Test
    void testGetOrdersSuccess() {
        OrderFilter filter = buildOrderFilter();

        PageRequest pageRequest = PageRequest.of(1, 10);
        List<Order> mockOrders = List.of(
                Order.builder()
                        .id(1)
                        .customer(Customer.builder().id(3).build())
                        .createdAt(LocalDateTime.now()).build(),
                Order.builder()
                        .id(2)
                        .customer(Customer.builder().id(3).build())
                        .createdAt(LocalDateTime.now().minusDays(1)).build()
        );
        Page<Order> mockPage = new PageImpl<>(mockOrders, pageRequest, 2);

        when(orderRepositoryFacade.findAllByCustomerIdAndCreationDate(any(OrderFilter.class), any(PageRequest.class)))
                .thenReturn(mockPage);

        OrdersResponse response = orderService.getOrders(filter);

        assertNotNull(response);
    }

    @Test
    void testGetOrdersFailure() {
        OrderFilter filter = buildOrderFilter();

        PageRequest pageRequest = PageRequest.of(1, 10);

        when(orderRepositoryFacade.findAllByCustomerIdAndCreationDate(any(OrderFilter.class), any(PageRequest.class)))
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.getOrders(filter));

        assertEquals("Database error", exception.getMessage());

    }

}
