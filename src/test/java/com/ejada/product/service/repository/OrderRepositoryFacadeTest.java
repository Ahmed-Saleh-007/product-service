package com.ejada.product.service.repository;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.model.entity.Order;
import com.ejada.product.service.model.filter.OrderFilter;
import com.ejada.product.service.repository.facade.OrderRepositoryFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.sql.SQLException;

import static com.ejada.product.service.util.Constants.DATABASE_GENERAL_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderRepositoryFacadeTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderRepositoryFacade orderRepositoryFacade;

    private Order order;

    @BeforeEach
    void setup() {
        order = new Order();
        order.setId(1);
        order.setStatus("completed");
        order.setTotalAmount(new BigDecimal("100.00"));
    }

    @Test
    void createOrderSuccess() {
        Mockito.when(orderRepository.save(order)).thenReturn(order);
        Order result = orderRepositoryFacade.createOrder(order);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(order.getId(), result.getId());
        Assertions.assertEquals(order.getStatus(), result.getStatus());
        Assertions.assertEquals(order.getTotalAmount(), result.getTotalAmount());
        Mockito.verify(orderRepository).save(order);
    }

    @Test
    void createOrder_exception() {
        Mockito.when(orderRepository.save(order)).thenThrow(new RuntimeException(DATABASE_GENERAL_ERROR_MESSAGE));
        assertThrows(BusinessException.class, () -> orderRepositoryFacade.createOrder(order));
        Mockito.verify(orderRepository).save(order);
    }

    @Test
    void testFindAllByCustomerIdAndCreationDateSuccess() {
        when(orderRepository.findAllByCustomerIdAndCreationDate(any(), any()))
                .thenReturn(Page.empty());
        assertDoesNotThrow(() -> orderRepositoryFacade.findAllByCustomerIdAndCreationDate(
                OrderFilter.builder().build(),
                PageRequest.of(0, 10)));
    }

    @Test
    void testFindAllByCustomerIdAndCreationDateFailsWithSqlException() {
        doAnswer(
                invocation -> {
                    throw new SQLException("");
                })
                .when(orderRepository)
                .findAllByCustomerIdAndCreationDate(any(), any());
        assertThrows(BusinessException.class, () ->
                orderRepositoryFacade.findAllByCustomerIdAndCreationDate(
                        OrderFilter.builder().build(),
                        PageRequest.of(0, 10)));
    }

}
