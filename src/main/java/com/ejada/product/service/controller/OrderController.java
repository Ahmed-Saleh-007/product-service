package com.ejada.product.service.controller;

import com.ejada.product.service.exception.ApiBusinessErrorResponse;
import com.ejada.product.service.model.filter.OrderFilter;
import com.ejada.product.service.model.request.CreateOrderRequest;
import com.ejada.product.service.model.response.CreateOrderResponse;
import com.ejada.product.service.model.response.OrderHistoryResponse;
import com.ejada.product.service.model.response.OrdersResponse;
import com.ejada.product.service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Validated
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(description = "Get Orders", summary = "Get Orders", tags = "Orders")
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(schema = @Schema(implementation = OrdersResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = @Content(schema = @Schema(implementation = ApiBusinessErrorResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad request",
            content = @Content(schema = @Schema(implementation = ApiBusinessErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden",
            content = @Content(schema = @Schema(implementation = ApiBusinessErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ApiBusinessErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ApiBusinessErrorResponse.class)))
    public ResponseEntity<OrdersResponse> getOrders(
            @Parameter(description = "Customer ID") @RequestParam(required = false) Integer customerId,
            @Parameter(description = "Min Total Amount") @RequestParam(required = false) LocalDate createdAtStart,
            @Parameter(description = "Max Total Amount") @RequestParam(required = false) LocalDate createdAtEnd,
            @Parameter(description = "Order Status") @RequestParam(required = false) String status,
            @Parameter(description = "Page Index") @RequestParam(defaultValue = "0") @Min(0) int pageIndex,
            @Parameter(description = "Page Size") @RequestParam(defaultValue = "10") @Min(1) int pageSize,
            @Parameter(description = "Sort Order") @RequestParam(required = false) String sortOrder,
            @Parameter(description = "Sort Field") @RequestParam(required = false) String sortField
    ) {
        OrderFilter orderFilter = OrderFilter.builder()
                .customerId(customerId)
                .createdAtStart(createdAtStart != null ? createdAtStart.atStartOfDay() : null)
                .createdAtEnd(createdAtEnd != null ? createdAtEnd.atStartOfDay()
                        .plusHours(23).plusMinutes(59).plusSeconds(59) : null)
                .status(status)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .sortOrder(sortOrder)
                .sortField(sortField)
                .build();
        OrdersResponse ordersResponse = orderService.getOrders(orderFilter);
        return ResponseEntity.ok().body(ordersResponse);
    }

    @PostMapping
    @Operation(description = "Create Order", summary = "Create Order", tags = "Orders")
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(schema = @Schema(implementation = CreateOrderResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = @Content(schema = @Schema(implementation = ApiBusinessErrorResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad request",
            content = @Content(schema = @Schema(implementation = ApiBusinessErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden",
            content = @Content(schema = @Schema(implementation = ApiBusinessErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ApiBusinessErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ApiBusinessErrorResponse.class)))
    public ResponseEntity<CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.createOrder(request));

    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<OrderHistoryResponse>> getOrderHistoryByCustomerId(@PathVariable int customerId) {
        List<OrderHistoryResponse> orderHistory = orderService.getOrderHistoryByCustomerId(customerId);
        return ResponseEntity.ok(orderHistory);
    }

}
