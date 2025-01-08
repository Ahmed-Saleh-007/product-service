package com.ejada.product.service.service;

import com.ejada.product.service.model.request.CreateOrderRequest;
import com.ejada.product.service.model.response.CreateOrderResponse;
import com.ejada.product.service.model.request.OrderProductRequest;
import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.exception.ErrorCodeEnum;
import com.ejada.product.service.model.entity.Order;
import com.ejada.product.service.model.filter.OrderFilter;
import com.ejada.product.service.model.mapper.OrderMapper;
import com.ejada.product.service.model.response.OrdersResponse;
import com.ejada.product.service.repository.facade.OrderRepositoryFacade;
import com.ejada.product.service.model.entity.Customer;
import com.ejada.product.service.model.entity.Order;
import com.ejada.product.service.model.entity.OrderProduct;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.repository.facade.CustomerRepositoryFacade;
import com.ejada.product.service.repository.facade.OrderRepositoryFacade;
import com.ejada.product.service.repository.facade.ProductRepositoryFacade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.ejada.product.service.util.Constants.INVALID_DATES_FILTER_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.SORT_ORDER_DESC;
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
import static com.ejada.product.service.util.Constants.PRODUCTS_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepositoryFacade orderRepositoryFacade;
    private final ProductRepositoryFacade productRepositoryFacade;
    private final CustomerRepositoryFacade customerRepositoryFacade;
    public OrdersResponse getOrders(OrderFilter orderFilter) {
        log.info("Get orders by filter: [{}]", orderFilter.toString());
        validateOrderFilter(orderFilter);
        Page<Order> orders = orderRepositoryFacade.findAllByCustomerIdAndCreationDate(orderFilter,
                getOrderPageRequest(orderFilter));

        return OrdersResponse.builder()
                .orders(OrderMapper.INSTANCE.mapToListGetOrdersResponse(orders.getContent()))
                .pageCount(orders.getTotalPages())
                .totalCount(orders.getTotalElements())
                .build();
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

    private List<Product> validateAllProductsInOrder(List<OrderProductRequest> orderProductRequests) {
        List<Integer> productIds = orderProductRequests.stream()
                .map(OrderProductRequest::getProductId)
                .toList();

        List<Product> products = productRepositoryFacade.findAllById(productIds);

        Map<Integer, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<Integer> nonExistentProducts = new ArrayList<>();
        List<Integer> outOfStockProducts = new ArrayList<>();

        orderProductRequests.forEach(dto -> {
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
    private List<OrderProduct> prepareOrderProducts(List<OrderProductRequest> orderProductRequests, List<Product> products) {
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (OrderProductRequest productDTO : orderProductRequests) {
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

    private void validateOrderFilter(OrderFilter orderFilter) {
        if(orderFilter.getCreatedAtEnd() != null && orderFilter.getCreatedAtStart() != null &&
        orderFilter.getCreatedAtEnd().isBefore(orderFilter.getCreatedAtStart())){
            throw BusinessException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errorCode(ErrorCodeEnum.BAD_REQUEST.getCode())
                    .message(INVALID_DATES_FILTER_ERROR_MESSAGE)
                    .build();
        }
    }

    private PageRequest getOrderPageRequest(OrderFilter orderFilter) {
        if (StringUtils.hasText(orderFilter.getSortField())) {
            Sort sort = SORT_ORDER_DESC.equalsIgnoreCase(orderFilter.getSortOrder())
                    ? Sort.by(orderFilter.getSortField()).descending()
                    : Sort.by(orderFilter.getSortField()).ascending();
            return PageRequest.of(orderFilter.getPageIndex(), orderFilter.getPageSize(), sort);
        } else {
            return PageRequest.of(orderFilter.getPageIndex(), orderFilter.getPageSize());
        }
    }

}
