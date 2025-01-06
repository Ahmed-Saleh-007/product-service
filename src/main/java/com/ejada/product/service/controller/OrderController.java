package com.ejada.product.service.controller;

import com.ejada.product.service.exception.ApiBusinessErrorResponse;
import com.ejada.product.service.model.dto.CreateOrderRequest;
import com.ejada.product.service.model.dto.CreateOrderResponse;
import com.ejada.product.service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@Validated
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(description = "Get Orders", summary = "Get Orders", tags = "Orders")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    @ApiResponse(responseCode = "406", description = "Not Acceptable")
    @ApiResponse(responseCode = "429", description = "Too Many Requests")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @ApiResponse(responseCode = "502", description = "Bad Gateway")
    public ResponseEntity<List<Objects>> getOrders() {
        return ResponseEntity.ok().body(orderService.getOrders());
    }

    @PostMapping
    @Operation(description = "Create Order", summary = "Create Order", tags = "Orders")
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(schema = @Schema(implementation = ApiBusinessErrorResponse.class)))
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
}
