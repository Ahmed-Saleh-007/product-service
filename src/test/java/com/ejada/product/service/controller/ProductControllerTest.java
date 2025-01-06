package com.ejada.product.service.controller;

import com.ejada.product.service.service.ProductService;
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

@WebMvcTest(ProductController.class)
@TestInstance(PER_CLASS)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private static final String PRODUCTS_BASE_URL = "/api/products";

    @Test
    void testGetProductsSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCTS_BASE_URL)
                        .param("categoryIds", "1", "2")
                        .param("minPrice", "100.0")
                        .param("maxPrice", "500.0")
                        .param("isInStock", "true")
                        .param("pageIndex", "1")
                        .param("pageSize", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
   @Test
    void testSoftDeleteProductSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(PRODUCTS_BASE_URL + "/{id}", 1)).andExpect(status().isOk());
    }

}
