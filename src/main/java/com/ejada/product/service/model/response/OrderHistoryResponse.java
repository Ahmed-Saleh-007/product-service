package com.ejada.product.service.model.response;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryResponse {
    private int customerId;
    private String customerName;
    private int orderId;
    private LocalDateTime orderDate;
    private String orderStatus;
    private Double totalCost;
    private  List<ProductDetailsResponse> productDetails;
}
