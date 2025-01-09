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
    void testGetProductsSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ORDERES_BASE_URL + "/{id}", 2)
                        .param("OredreId", "5")
                        .param("OrderDate", "2025-01-09T11:21:50.939231")
                        .param("OrderStatus", "completed")
                        .param("TotalCost", "500.0")
                        .param("DiscountAmount", "0.00")
                        .param("TotalAmountAfterDiscount", "500.00")
                        .param("ProductDetails", "{\n" +
                                "                \"ProductName\": \"AB\",\n" +
                                "                \"Quantity\": 5,\n" +
                                "                \"Price\": 100.00,\n" +
                                "                \"Subtotal\": 500.00,\n" +
                                "                \"CategoryName\": \"Home & Kitchen\"\n" +
                                "            }")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
