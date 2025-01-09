package com.ejada.product.service.service;


import com.ejada.product.service.exception.CommonExceptionHandler;
import com.ejada.product.service.model.entity.Customer;
import com.ejada.product.service.model.entity.Order;
import com.ejada.product.service.model.entity.OrderProduct;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.model.entity.Promotion;
import com.ejada.product.service.model.enums.DiscountTypeEnum;
import com.ejada.product.service.model.filter.OrderFilter;
import com.ejada.product.service.model.mapper.OrderMapper;
import com.ejada.product.service.model.request.CreateOrderRequest;
import com.ejada.product.service.model.request.OrderProductRequest;
import com.ejada.product.service.model.response.CreateOrderResponse;
import com.ejada.product.service.model.response.OrderHistoryResponse;
import com.ejada.product.service.model.response.OrdersResponse;
import com.ejada.product.service.repository.OrderRepository;
import com.ejada.product.service.repository.facade.CustomerRepositoryFacade;
import com.ejada.product.service.repository.facade.OrderRepositoryFacade;
import com.ejada.product.service.repository.facade.ProductRepositoryFacade;
import com.ejada.product.service.repository.facade.PromotionRepositoryFacade;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.ejada.product.service.exception.CommonExceptionHandler.handleBadRequestException;
import static com.ejada.product.service.exception.CommonExceptionHandler.handleInternalServerErrorException;
import static com.ejada.product.service.util.Constants.ACTIVE_PROMOTION_NOT_FOUND;
import static com.ejada.product.service.util.Constants.CUSTOMER_NOT_FOUND;
import static com.ejada.product.service.util.Constants.INSUFFICIENT_STOCK;
import static com.ejada.product.service.util.Constants.INTERNAL_SERVER_ERROR;
import static com.ejada.product.service.util.Constants.INVALID_DATES_FILTER_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.PRODUCTS_NOT_FOUND;
import static com.ejada.product.service.util.Constants.SORT_ORDER_DESC;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepositoryFacade productRepositoryFacade;
    private final OrderRepositoryFacade orderRepositoryFacade;
    private final CustomerRepositoryFacade customerRepositoryFacade;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PromotionRepositoryFacade promotionRepositoryFacade;

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
        // Find active promotion by code
        Promotion promotion = findActivePromotionByCode(request.getPromotionCode());
        // Prepare order products and update stock
        List<OrderProduct> orderProducts = prepareOrderProducts(request.getProducts(), products);
        // Calculate amounts
        BigDecimal totalAmount = calculateTotalAmount(orderProducts);
        BigDecimal discountAmount = calculateDiscountAmount(promotion, totalAmount);
        // Save order
        Order order = saveOrder(customer, orderProducts, promotion, totalAmount, discountAmount);
        return CreateOrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .discountAmount(order.getDiscountAmount())
                .totalAmountAfterDiscount(order.getTotalAmountAfterDiscount())
                .status(order.getStatus())
                .build();
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
            throw handleBadRequestException(PRODUCTS_NOT_FOUND + nonExistentProducts);
        }
        if (!outOfStockProducts.isEmpty()) {
            throw handleBadRequestException(INSUFFICIENT_STOCK + outOfStockProducts);
        }

        return products;
    }

    private Promotion findActivePromotionByCode(String code) {
        return StringUtils.hasText(code)
                ? promotionRepositoryFacade.findActiveByCode(code)
                .orElseThrow(() -> handleBadRequestException(ACTIVE_PROMOTION_NOT_FOUND))
                : null;
    }

    private List<OrderProduct> prepareOrderProducts(List<OrderProductRequest> orderProductRequests, List<Product> products) {
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (OrderProductRequest orderProductRequest : orderProductRequests) {
            Product product = products.stream()
                    .filter(p -> p.getId().equals(orderProductRequest.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> handleInternalServerErrorException(INTERNAL_SERVER_ERROR));

            // update stock
            product.setStockQuantity(product.getStockQuantity() - orderProductRequest.getQuantity());
            productRepositoryFacade.updateProduct(product);

            // Create OrderProduct
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setQuantity(orderProductRequest.getQuantity());
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

    private BigDecimal calculateDiscountAmount(Promotion promotion, BigDecimal totalAmount) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (promotion != null) {
            if (promotion.getDiscountType() == DiscountTypeEnum.PERCENTAGE) {
                discountAmount = totalAmount
                        .multiply(promotion.getDiscountValue())
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN);
            } else if (promotion.getDiscountType() == DiscountTypeEnum.FLAT_AMOUNT) {
                discountAmount = promotion.getDiscountValue();
            }
            if (discountAmount.compareTo(totalAmount) > 0) {
                discountAmount = totalAmount;
            }
        }
        return discountAmount;
    }

    private Order saveOrder(Customer customer, List<OrderProduct> orderProducts, Promotion promotion,
                            BigDecimal totalAmount, BigDecimal discountAmount) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setTotalAmountAfterDiscount(totalAmount.subtract(discountAmount));
        order.setStatus("completed");
        order.setOrderProducts(orderProducts);
        order.setPromotion(promotion);
        orderProducts.forEach(op -> op.setOrder(order));
        return orderRepositoryFacade.createOrder(order);
    }

    private void validateOrderFilter(OrderFilter orderFilter) {
        if (orderFilter.getCreatedAtEnd() != null && orderFilter.getCreatedAtStart() != null &&
                orderFilter.getCreatedAtEnd().isBefore(orderFilter.getCreatedAtStart())) {
            throw CommonExceptionHandler.handleBadRequestException(INVALID_DATES_FILTER_ERROR_MESSAGE);
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

    public List<OrderHistoryResponse> getOrderHistoryByCustomerId(int customerId) {
        return orderMapper.mapToListOrderHistoryResponse(orderRepository.findAllByCustomerId(customerId));
    }

}


