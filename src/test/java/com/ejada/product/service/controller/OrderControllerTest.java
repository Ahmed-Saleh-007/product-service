package com.ejada.product.service.controller;

import com.ejada.product.service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@TestInstance(PER_CLASS)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    private static final String ORDERES_BASE_URL = "/api/orders";

    @Test
    void testGetOrderHistorySuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ORDERES_BASE_URL + "/{id}", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
