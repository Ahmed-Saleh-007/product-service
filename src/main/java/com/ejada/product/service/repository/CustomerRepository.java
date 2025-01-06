package com.ejada.product.service.repository;

import com.ejada.product.service.model.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
}
