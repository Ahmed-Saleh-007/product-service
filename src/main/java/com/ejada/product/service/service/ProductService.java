package com.ejada.product.service.service;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.exception.ErrorCodeEnum;
import com.ejada.product.service.model.dto.CreateProductRequest;
import com.ejada.product.service.model.dto.CreateProductResponse;
import com.ejada.product.service.model.dto.ProductEntityMapper;
import com.ejada.product.service.model.entity.Category;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.model.filter.ProductFilter;
import com.ejada.product.service.model.mapper.ProductMapper;
import com.ejada.product.service.model.response.ProductWithPagingResponse;
import com.ejada.product.service.repository.CategoryRepository;
import com.ejada.product.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.ejada.product.service.util.Constants.INVALID_PRODUCT_PRICE_FILTER_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.SORT_ORDER_DESC;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    public ProductWithPagingResponse getProducts(ProductFilter productFilter) {
        log.info("Get products by filter: [{}]", productFilter.toString());
        validateProductFilter(productFilter);
        Page<Product> products = productRepository
                .findAllByCategoryAndPriceRange(productFilter, getProductPageRequest(productFilter));
        return ProductWithPagingResponse.builder()
                .products(productMapper.mapToProductResponse(products.getContent()))
                .pageCount(products.getTotalPages())
                .totalCount(products.getTotalElements())
                .build();
    }

    public CreateProductResponse createProduct(CreateProductRequest request) {
        Product productEntity = ProductEntityMapper.INSTANCE.mapToProductEntity(request);
        Optional<Category> category = categoryRepository.findById(request.getCategoryId());
        productEntity.setCategory(category.orElse(Category.builder().id(0).build()));
        productRepository.save(productEntity);
        return CreateProductResponse.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .category(category.orElse(null))
                .quantity(request.getQuantity())
                .build();
    }

    private void validateProductFilter(ProductFilter productFilter) {
        if (productFilter.getMinPrice() != null && productFilter.getMaxPrice() != null
                && productFilter.getMinPrice() > productFilter.getMaxPrice()) {
            throw BusinessException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errorCode(ErrorCodeEnum.BAD_REQUEST.getCode())
                    .message(INVALID_PRODUCT_PRICE_FILTER_ERROR_MESSAGE)
                    .build();
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

}
