package com.ejada.product.service.repository;

import com.ejada.product.service.model.entity.Order;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


    @Repository
    public interface OrderRepository extends CrudRepository<Order, Integer> {

        @Query(value = """
        SELECT 
            c.id AS customer_id, 
            c.name AS customer_name, 
            o.id AS order_id, 
            o.created_at AS order_date, 
            o.status AS order_status, 
            o.total_amount AS total_cost, 
            p.name AS product_name, 
            op.quantity AS product_quantity, 
            op.price AS product_price, 
            (op.quantity * op.price) AS subtotal, 
            cat.name AS category_name 
        FROM customers c 
        JOIN orders o ON c.id = o.customer_id 
        JOIN order_products op ON o.id = op.order_id 
        JOIN products p ON op.product_id = p.id 
        JOIN categories cat ON p.category_id = cat.id 
        WHERE c.id = :customerId 
        ORDER BY o.created_at DESC
    """, nativeQuery = true)
        List<Map<String, Object>> findOrderHistoryByCustomerId(@Param("customerId") int customerId);
    }
