package com.ejada.product.service.model.mapper;

import com.ejada.product.service.model.entity.Order;
import com.ejada.product.service.model.entity.OrderProduct;
import com.ejada.product.service.model.response.GetOrdersOrderProductResponse;
import com.ejada.product.service.model.response.GetOrdersResponse;
import com.ejada.product.service.model.response.OrderHistoryResponse;
import com.ejada.product.service.model.response.ProductDetailsResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")

public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);


    @Mapping(target = "productId", expression = "java(orderProduct.getProduct().getId())")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "productName", expression = "java(orderProduct.getProduct().getName())")
    GetOrdersOrderProductResponse mapToGetOrdersOrderProductDto(OrderProduct orderProduct);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "discountAmount", source = "discountAmount")
    @Mapping(target = "totalAmountAfterDiscount", source = "totalAmountAfterDiscount")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "orderProducts", source = "orderProducts")
    GetOrdersResponse mapToGetOrdersResponse(Order order);

    List<GetOrdersResponse> mapToListGetOrdersResponse(List<Order> orders);

    List<OrderHistoryResponse> mapToListOrderHistoryResponse(List<Order> orders);
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "orderDate", source = "createdAt")
    @Mapping(target = "orderStatus", source = "status")
    @Mapping(target = "totalCost", source = "totalAmountAfterDiscount")
    @Mapping(target = "productDetails", source = "orderProducts")
    OrderHistoryResponse mapToListOrderHistoryResponse(Order order);

    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "categoryName", source = "product.category.name")
    @Mapping(target = "subtotal",expression = "java(orderProduct.getPrice().multiply(java.math.BigDecimal.valueOf(orderProduct.getQuantity())))")
    ProductDetailsResponse mapToProductDetailsResponse(OrderProduct orderProduct);


}
