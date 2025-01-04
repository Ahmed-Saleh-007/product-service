package com.ejada.product.service.repository;

import com.ejada.product.service.model.entity.Product;
import com.ejada.product.service.model.filter.ProductFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

    @EntityGraph(attributePaths = {"category"})
    @Query("""
            SELECT product FROM Product product
            WHERE (:#{#productFilter.categoryIds == null || #productFilter.categoryIds.isEmpty()} IS TRUE
            OR product.category.id IN :#{#productFilter.categoryIds})
            AND product.deletedAt IS NULL
            AND (:#{#productFilter.isInStock} IS FALSE OR product.stockQuantity > 0)
            AND (:#{#productFilter.minPrice} IS NULL OR product.price >= :#{#productFilter.minPrice})
            AND (:#{#productFilter.maxPrice} IS NULL OR product.price <= :#{#productFilter.maxPrice})
            """)
    Page<Product> findAllByCategoryAndPriceRange(ProductFilter productFilter, Pageable pageable);

}
