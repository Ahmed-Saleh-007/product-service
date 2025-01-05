package com.ejada.product.service.service;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.exception.ErrorCodeEnum;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.model.filter.ProductFilter;
import com.ejada.product.service.model.mapper.ProductMapper;
import com.ejada.product.service.model.response.ProductWithPagingResponse;
import com.ejada.product.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.ejada.product.service.util.Constants.INVALID_PRODUCT_PRICE_FILTER_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.SORT_ORDER_DESC;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

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
