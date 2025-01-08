package com.ejada.product.service.service;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.model.dto.CreateOrderRequest;
import com.ejada.product.service.model.dto.CreateOrderResponse;
import com.ejada.product.service.model.dto.OrderProductDto;
import com.ejada.product.service.model.entity.Customer;
import com.ejada.product.service.model.entity.Order;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.repository.facade.CustomerRepositoryFacade;
import com.ejada.product.service.repository.facade.OrderRepositoryFacade;
import com.ejada.product.service.repository.facade.ProductRepositoryFacade;
import org.junit.jupiter.api.Assertions;
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
import static com.ejada.product.service.utils.TestUtils.buildCustomer;
import static com.ejada.product.service.utils.TestUtils.buildOrderRequest;
import static com.ejada.product.service.utils.TestUtils.buildProduct;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private CustomerRepositoryFacade customerRepositoryFacade;

    @Mock
    private ProductRepositoryFacade productRepositoryFacade;

    @Mock
    private OrderRepositoryFacade orderRepositoryFacade;

    @InjectMocks
    private OrderService orderService;

    // Test data setup
    private CreateOrderRequest createOrderRequest;
    private Customer customer;
    private Product product;
    private List<OrderProductDto> orderProductDtos;

    @BeforeEach
    void setup() {
        customer = buildCustomer();

        product = buildProduct();

        createOrderRequest = buildOrderRequest();
    }
    @Test
    void createOrder_success() {
        Mockito.when(customerRepositoryFacade.findById(1)).thenReturn(Optional.of(customer));
        Mockito.when(productRepositoryFacade.findAllById(List.of(1))).thenReturn(List.of(product));
        Mockito.when(orderRepositoryFacade.createOrder(Mockito.any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1);
            return order;
        });

        CreateOrderResponse response = orderService.createOrder(createOrderRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getOrderId());
        Assertions.assertEquals(new BigDecimal("59.98"), response.getTotalAmount());

        Mockito.verify(customerRepositoryFacade).findById(1);
        Mockito.verify(productRepositoryFacade).findAllById(List.of(1));
        Mockito.verify(orderRepositoryFacade).createOrder(Mockito.any(Order.class));
    }
    @Test
    void createOrder_customerNotFound() {
        Mockito.when(customerRepositoryFacade.findById(1)).thenReturn(Optional.empty());

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            orderService.createOrder(createOrderRequest);
        });

        Mockito.verify(customerRepositoryFacade).findById(1);
        Mockito.verifyNoInteractions(productRepositoryFacade, orderRepositoryFacade);
    }

    @Test
    void createOrder_productNotFound() {
        Mockito.when(customerRepositoryFacade.findById(1)).thenReturn(Optional.of(customer));
        Mockito.when(productRepositoryFacade.findAllById(List.of(1))).thenReturn(Collections.emptyList());

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            orderService.createOrder(createOrderRequest);
        });

        Mockito.verify(productRepositoryFacade).findAllById(List.of(1));
        Mockito.verifyNoInteractions(orderRepositoryFacade);
    }

    @Test
    void createOrder_insufficientStock() {
        product.setStockQuantity(1);

        Mockito.when(customerRepositoryFacade.findById(1)).thenReturn(Optional.of(customer));
        Mockito.when(productRepositoryFacade.findAllById(List.of(1))).thenReturn(List.of(product));

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            orderService.createOrder(createOrderRequest);
        });

        Mockito.verify(productRepositoryFacade).findAllById(List.of(1));
        Mockito.verifyNoInteractions(orderRepositoryFacade);
    }
}
