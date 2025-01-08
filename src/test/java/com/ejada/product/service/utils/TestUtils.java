package com.ejada.product.service.utils;

import com.ejada.product.service.model.request.CreateOrderRequest;
import com.ejada.product.service.model.request.OrderProductRequest;
import com.ejada.product.service.model.entity.Customer;
import com.ejada.product.service.model.entity.Product;

import java.util.List;

import static com.ejada.product.service.utils.TestConstants.CUSTOMER_ID;
import static com.ejada.product.service.utils.TestConstants.CUSTOMER_NAME;
import static com.ejada.product.service.utils.TestConstants.PRODUCT_ID;
import static com.ejada.product.service.utils.TestConstants.PRODUCT_NAME;
import static com.ejada.product.service.utils.TestConstants.PRODUCT_PRICE;
import static com.ejada.product.service.utils.TestConstants.PRODUCT_STOCK_QUANTITY;

public class TestUtils {
    public static Product buildProduct()
    {
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setName(PRODUCT_NAME);
        product.setStockQuantity(PRODUCT_STOCK_QUANTITY);
        product.setPrice(PRODUCT_PRICE);
        return product;
    }

    public static Customer buildCustomer()
    {
        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setName(CUSTOMER_NAME);
        return customer;
    }

    public static CreateOrderRequest buildOrderRequest()
    {
        OrderProductRequest orderProductRequest = new OrderProductRequest(1, 2);
        List<OrderProductRequest> orderProductRequests = List.of(orderProductRequest);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setCustomerId(CUSTOMER_ID);
        createOrderRequest.setProducts(orderProductRequests);

        return createOrderRequest;
    }
}
