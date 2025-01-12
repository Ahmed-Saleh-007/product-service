package com.ejada.product.service.controller;

import com.ejada.product.service.model.request.CreateOrderRequest;
import com.ejada.product.service.model.response.CreateOrderResponse;
import com.ejada.product.service.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static com.ejada.product.service.utils.TestUtils.buildOrderRequest;
import static com.ejada.product.service.utils.TestUtils.buildOrderResponse;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@TestInstance(PER_CLASS)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;
    private static final String ORDERS_BASE_URL = "/api/orders";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetOrdersSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ORDERS_BASE_URL)
                        .param("createdAtStart", "2025-01-10")
                        .param("createdAtEnd", "2025-01-11")
                        .param("customerId", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void testGetOrderHistorySuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ORDERS_BASE_URL + "/{id}", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createOrderSuccess() throws Exception {
        CreateOrderRequest request = buildOrderRequest();
        CreateOrderResponse response = buildOrderResponse();

        when(orderService.createOrder(any(CreateOrderRequest.class)))
                .thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post(ORDERS_BASE_URL)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(orderService, times(1)).createOrder(any(CreateOrderRequest.class));
    }

    @Test
    void createOrderFailureValidationError() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setProducts(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.post(ORDERS_BASE_URL)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
