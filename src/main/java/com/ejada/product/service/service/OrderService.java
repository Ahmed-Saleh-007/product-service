package com.ejada.product.service.service;

import com.ejada.product.service.model.dto.CreateOrderRequest;
import com.ejada.product.service.model.dto.CreateOrderResponse;
import com.ejada.product.service.model.dto.OrderHistoryDTO;
import com.ejada.product.service.model.dto.OrderProductDto;
import com.ejada.product.service.model.dto.ProductDetailsDTO;
import com.ejada.product.service.model.entity.Customer;
import com.ejada.product.service.model.entity.Order;
import com.ejada.product.service.model.entity.OrderProduct;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.repository.facade.CustomerRepositoryFacade;
import com.ejada.product.service.repository.facade.OrderRepositoryFacade;
import com.ejada.product.service.repository.facade.ProductRepositoryFacade;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.ejada.product.service.repository.OrderRepository;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ejada.product.service.exception.CommonExceptionHandler.handleBadRequestException;
import static com.ejada.product.service.exception.CommonExceptionHandler.handleInternalServerErrorException;
import static com.ejada.product.service.util.Constants.CUSTOMER_NOT_FOUND;
import static com.ejada.product.service.util.Constants.INSUFFICIENT_STOCK;
import static com.ejada.product.service.util.Constants.INTERNAL_SERVER_ERROR;
import static com.ejada.product.service.util.Constants.NO_ORDER_HISTORY_FOUND;
import static com.ejada.product.service.util.Constants.PRODUCTS_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepositoryFacade productRepositoryFacade;
    private final OrderRepositoryFacade orderRepositoryFacade;
    private final CustomerRepositoryFacade customerRepositoryFacade;
    private final OrderRepository orderRepository;

    public List<OrderHistoryDTO> getOrderHistoryByCustomerId(int customerId) {


        List<Map<String, Object>> rows = orderRepository.findOrderHistoryByCustomerId(customerId);

        if (rows == null || rows.isEmpty()) {
            throw handleBadRequestException(NO_ORDER_HISTORY_FOUND + customerId);
        }
        
        Map<Integer, OrderHistoryDTO> orderMap = new HashMap<>();
        for (Map<String, Object> row : rows) {
            int orderId = (int) row.get("order_id");

            OrderHistoryDTO orderDTO = orderMap.computeIfAbsent(orderId, id -> {
                OrderHistoryDTO orderHistory = new OrderHistoryDTO();
                orderHistory.setOrderId(orderId);
                orderHistory.setCustomerName((String) row.get("customer_name"));
                orderHistory.setOrderDate(((Timestamp) row.get("order_date")).toLocalDateTime());
                orderHistory.setCustomerId((int) row.get("customer_id"));
                orderHistory.setOrderStatus((String) row.get("order_status"));
                orderHistory.setTotalCost(((BigDecimal) row.get("total_cost")).doubleValue());
                orderHistory.setProductDetails(new ArrayList<>());
                return orderHistory;
            });

            // Map product details
            ProductDetailsDTO productDetail = new ProductDetailsDTO();
            productDetail.setProductName((String) row.get("product_name"));
            productDetail.setQuantity((Integer) row.get("product_quantity"));
            productDetail.setPrice(((BigDecimal) row.get("product_price")).doubleValue());
            productDetail.setSubtotal(((BigDecimal) row.get("subtotal")).doubleValue());
            productDetail.setCategoryName((String) row.get("category_name"));


            orderDTO.getProductDetails().add(productDetail);
        }

        return new ArrayList<>(orderMap.values());
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        Customer customer = customerRepositoryFacade.findById(request.getCustomerId())
                .orElseThrow(() -> handleBadRequestException(CUSTOMER_NOT_FOUND));
        // validate products existence and stock
        List<Product> products = validateAllProductsInOrder(request.getProducts());
        // Prepare order products and update stock
        List<OrderProduct> orderProducts = prepareOrderProducts(request.getProducts(), products);
        // Calculate total amount
        BigDecimal totalAmount = calculateTotalAmount(orderProducts);
        // Save order
        Order order = saveOrder(customer, orderProducts, totalAmount);
        return new CreateOrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }

    private List<Product> validateAllProductsInOrder(List<OrderProductDto> orderProductDtos) {
        List<Integer> productIds = orderProductDtos.stream()
                .map(OrderProductDto::getProductId)
                .toList();

        List<Product> products = productRepositoryFacade.findAllById(productIds);

        Map<Integer, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<Integer> nonExistentProducts = new ArrayList<>();
        List<Integer> outOfStockProducts = new ArrayList<>();

        orderProductDtos.forEach(dto -> {
            Product product = productMap.get(dto.getProductId());
            if (product == null) {
                nonExistentProducts.add(dto.getProductId());
            } else if (product.getStockQuantity() < dto.getQuantity()) {
                outOfStockProducts.add(dto.getProductId());
            }
        });

        if (!nonExistentProducts.isEmpty()) {
            throw handleBadRequestException(PRODUCTS_NOT_FOUND+ nonExistentProducts);
        }
        if (!outOfStockProducts.isEmpty()) {
            throw handleBadRequestException(INSUFFICIENT_STOCK+ outOfStockProducts);
        }

        return products;
    }
    private List<OrderProduct> prepareOrderProducts(List<OrderProductDto> orderProductDtos, List<Product> products) {
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (OrderProductDto productDTO : orderProductDtos) {
            Product product = products.stream()
                    .filter(p -> p.getId().equals(productDTO.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> handleInternalServerErrorException(INTERNAL_SERVER_ERROR));

            // update stock
            product.setStockQuantity(product.getStockQuantity() - productDTO.getQuantity());
            productRepositoryFacade.updateProduct(product);

            // Create OrderProduct
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setQuantity(productDTO.getQuantity());
            orderProduct.setPrice(product.getPrice());
            orderProducts.add(orderProduct);
        }
        return orderProducts;
    }
    private BigDecimal calculateTotalAmount(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(op -> op.getPrice().multiply(BigDecimal.valueOf(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    private Order saveOrder(Customer customer, List<OrderProduct> orderProducts, BigDecimal totalAmount) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setTotalAmount(totalAmount);
        order.setStatus("completed");
        order.setOrderProducts(orderProducts);
        orderProducts.forEach(op -> op.setOrder(order));
        return orderRepositoryFacade.createOrder(order);
    }

    public List<Objects> getOrders() {
        log.info("Get Orders");
        return List.of();
    }

}
