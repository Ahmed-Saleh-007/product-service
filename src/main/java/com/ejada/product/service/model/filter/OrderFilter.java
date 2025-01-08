package com.ejada.product.service.model.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderFilter {
    private Integer customerId;
    private LocalDateTime createdAtStart;
    private LocalDateTime createdAtEnd;
    private String status;
    private int pageIndex;
    private int pageSize;
    private String sortOrder;
    private String sortField;
}
