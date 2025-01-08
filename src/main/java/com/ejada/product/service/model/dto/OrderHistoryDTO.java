package com.ejada.product.service.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderHistoryDTO {
    private int customerId;
    private String customerName;
    private int orderId;
    private LocalDateTime orderDate;
    private String orderStatus;
    private Double totalCost;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public List<ProductDetailsDTO> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetailsDTO> productDetails) {
        this.productDetails = productDetails;
    }

    private List<ProductDetailsDTO> productDetails;


}
