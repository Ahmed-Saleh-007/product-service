package com.ejada.product.service.service;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.exception.ErrorCodeEnum;
import com.ejada.product.service.model.entity.Category;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.model.filter.ProductFilter;
import com.ejada.product.service.model.mapper.ProductMapper;
import com.ejada.product.service.model.request.CreateProductRequest;
import com.ejada.product.service.model.response.CreateProductResponse;
import com.ejada.product.service.model.response.ProductWithPagingResponse;
import com.ejada.product.service.repository.facade.CategoryRepositoryFacade;
import com.ejada.product.service.repository.facade.ProductRepositoryFacade;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.ejada.product.service.exception.CommonExceptionHandler.handleBadRequestException;
import static com.ejada.product.service.exception.CommonExceptionHandler.handleInternalServerErrorException;
import static com.ejada.product.service.util.Constants.INVALID_CATEGORY_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.INVALID_PRODUCT_PRICE_FILTER_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.PRODUCT_ALREADY_EXISTS_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.PRODUCT_IS_ALREADY_DELETED;
import static com.ejada.product.service.util.Constants.PRODUCT_NOT_FOUND_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.SORT_ORDER_DESC;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepositoryFacade productRepositoryFacade;
    private final CategoryRepositoryFacade categoryRepositoryFacade;
    private final ProductMapper productMapper;

    public ProductWithPagingResponse getProducts(ProductFilter productFilter) {
        log.info("Get products by filter: [{}]", productFilter.toString());
        validateProductFilter(productFilter);
        Page<Product> products = productRepositoryFacade
                .findAllByCategoryAndPriceRange(productFilter, getProductPageRequest(productFilter));
        return ProductWithPagingResponse.builder()
                .products(productMapper.mapToProductResponse(products.getContent()))
                .pageCount(products.getTotalPages())
                .totalCount(products.getTotalElements())
                .build();
    }

    public CreateProductResponse createProduct(CreateProductRequest request) {
        Optional<Product> existingProduct = productRepositoryFacade.findByName(request.getName());
        validateProductName(existingProduct);
        Product productEntity = ProductMapper.INSTANCE.mapToProductEntity(request);
        Optional<Category> category = categoryRepositoryFacade.findById(request.getCategoryId());
        validateCategory(category);
        productEntity.setCategory(category.get());
        productRepositoryFacade.save(productEntity);
        return CreateProductResponse.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .category(category.orElse(null))
                .quantity(request.getQuantity())
                .build();
    }

    // Soft delete
    public void softDeleteProduct(int productId) {
        Product product = productRepositoryFacade.findProductById(productId)
                .orElseThrow(() -> handleInternalServerErrorException(PRODUCT_NOT_FOUND_ERROR_MESSAGE + productId));
        if (product.getDeletedAt() == null) {
            product.setDeletedAt(LocalDateTime.now());
            productRepositoryFacade.updateProduct(product);
        } else {
            throw handleInternalServerErrorException(PRODUCT_IS_ALREADY_DELETED);
        }
    }

    private void validateProductFilter(ProductFilter productFilter) {
        if (productFilter.getMinPrice() != null && productFilter.getMaxPrice() != null
                && productFilter.getMinPrice() > productFilter.getMaxPrice()) {
            throw handleBadRequestException(INVALID_PRODUCT_PRICE_FILTER_ERROR_MESSAGE);
        }
    }

    private PageRequest getProductPageRequest(ProductFilter productFilter) {
        if (StringUtils.hasText(productFilter.getSortField())) {
            Sort sort = SORT_ORDER_DESC.equalsIgnoreCase(productFilter.getSortOrder())
                    ? Sort.by(productFilter.getSortField()).descending()
                    : Sort.by(productFilter.getSortField()).ascending();
            return PageRequest.of(productFilter.getPageIndex(), productFilter.getPageSize(), sort);
        } else {
            return PageRequest.of(productFilter.getPageIndex(), productFilter.getPageSize());
        }
    }

    private void validateCategory(Optional<Category> category) {
        if (category.isEmpty()) {
            throw BusinessException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errorCode(ErrorCodeEnum.BAD_REQUEST.getCode())
                    .message(INVALID_CATEGORY_ERROR_MESSAGE)
                    .build();
        }
    }

    private void validateProductName(Optional<Product> product) {
        if (product.isPresent()) {
            throw BusinessException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errorCode(ErrorCodeEnum.BAD_REQUEST.getCode())
                    .message(PRODUCT_ALREADY_EXISTS_ERROR_MESSAGE)
                    .build();
        }
    }

}
