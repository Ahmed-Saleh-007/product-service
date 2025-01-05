package com.ejada.product.service.repository.facade;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.exception.ErrorCodeEnum;
import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.model.filter.ProductFilter;
import com.ejada.product.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.ejada.product.service.util.Constants.DATABASE_GENERAL_ERROR_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductRepositoryFacade {

    private final ProductRepository productRepository;

    public Page<Product> findAllByCategoryAndPriceRange(ProductFilter productFilter, Pageable pageable) {
        log.info("Find all products by category and price range: [{}]", productFilter.toString());
        try {
            return productRepository.findAllByCategoryAndPriceRange(productFilter, pageable);
        } catch (Exception e) {
            log.error("Error occurred while finding products by category and price range: [{}]", e.getMessage());
            throw BusinessException.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorCode(ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode())
                    .message(DATABASE_GENERAL_ERROR_MESSAGE)
                    .build();
        }
    }

}
