package com.ejada.product.service.repository;

import com.ejada.product.service.model.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {
}
