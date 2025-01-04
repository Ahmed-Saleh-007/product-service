package com.ejada.product.service.controller;

import com.ejada.product.service.exception.ApiBusinessErrorResponse;
import com.ejada.product.service.model.filter.ProductFilter;
import com.ejada.product.service.model.response.ProductWithPagingResponse;
import com.ejada.product.service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(description = "Get Products", summary = "Get Products", tags = "Products")
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(schema = @Schema(implementation = ProductWithPagingResponse.class)))
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
    public ResponseEntity<ProductWithPagingResponse> getProducts(
            @Parameter(description = "Category Ids") @RequestParam(required = false) List<Long> categoryIds,
            @Parameter(description = "Minimum Product Price") @RequestParam(required = false) @Min(0) Double minPrice,
            @Parameter(description = "Maximum Product Price") @RequestParam(required = false) @Min(0) Double maxPrice,
            @Parameter(description = "Product Availability") @RequestParam(defaultValue = "false") boolean isInStock,
            @Parameter(description = "Page Number") @RequestParam(defaultValue = "0") @Min(0) int pageNumber,
            @Parameter(description = "Page Size") @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        ProductFilter productFilter = ProductFilter.builder()
                .categoryIds(categoryIds)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .isInStock(isInStock)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts(productFilter));
    }

}
