package com.ejada.product.service.controller;

import com.ejada.product.service.exception.ApiBusinessErrorResponse;
import com.ejada.product.service.model.dto.CreateProductRequest;
import com.ejada.product.service.model.dto.CreateProductResponse;
import com.ejada.product.service.model.filter.ProductFilter;
import com.ejada.product.service.model.response.ProductWithPagingResponse;
import com.ejada.product.service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            @Parameter(description = "Is In Stock") @RequestParam(defaultValue = "false") boolean isInStock,
            @Parameter(description = "Page Index") @RequestParam(defaultValue = "0") @Min(0) int pageIndex,
            @Parameter(description = "Page Size") @RequestParam(defaultValue = "10") @Min(1) int pageSize,
            @Parameter(description = "Sort Order") @RequestParam(required = false) String sortOrder,
            @Parameter(description = "Sort Field") @RequestParam(required = false) String sortField) {
        ProductFilter productFilter = ProductFilter.builder()
                .categoryIds(categoryIds)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .isInStock(isInStock)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .sortOrder(sortOrder)
                .sortField(sortField)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts(productFilter));
    }

    @PostMapping
    @Operation(description = "Create Product", summary = "Create Product", tags = "Products")
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
    public ResponseEntity<CreateProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.createProduct(request));
    }
    @PutMapping("softDelete/{productId}")
    public ResponseEntity<String> softDelete(@PathVariable int productId) {
        productService.softDeleteProduct(productId);
        return ResponseEntity.ok("Product soft deleted successfully."+productId);
    }

}
