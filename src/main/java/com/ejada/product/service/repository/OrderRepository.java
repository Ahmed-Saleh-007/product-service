package com.ejada.product.service.repository;

import com.ejada.product.service.model.entity.Order;
import com.ejada.product.service.model.filter.OrderFilter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
    @EntityGraph(attributePaths = {"customer"})
    @Query("""
            SELECT order FROM Order order
            WHERE (:#{#orderFilter.customerId == null} IS TRUE
            OR order.customer.id IN :#{#orderFilter.customerId})
            AND (:#{#orderFilter.createdAtStart} IS NULL OR order.createdAt >= :#{#orderFilter.createdAtStart})
            AND (:#{#orderFilter.createdAtEnd} IS NULL OR order.createdAt <= :#{#orderFilter.createdAtEnd})
            """)
    Page<Order> findAllByCustomerIdAndCreationDate(OrderFilter orderFilter, Pageable pageable);


    @EntityGraph(attributePaths = {"orderProducts.product.category"})
    List<Order> findAllByCustomerId(Integer customerId);
}

