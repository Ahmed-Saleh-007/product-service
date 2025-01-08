package com.ejada.product.service.repository.facade;

import com.ejada.product.service.model.entity.Order;
import com.ejada.product.service.model.filter.OrderFilter;
import com.ejada.product.service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.ejada.product.service.exception.CommonExceptionHandler.handleInternalServerErrorException;
import static com.ejada.product.service.util.Constants.DATABASE_GENERAL_ERROR_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderRepositoryFacade {
    private final OrderRepository orderRepository;

    public Order createOrder(Order order) {
        log.info("create order OrderRepositoryFacade: [{}]", order);
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            log.error("Error occurred while creating order OrderRepositoryFacade: [{}]", e.getMessage());
            throw handleInternalServerErrorException(DATABASE_GENERAL_ERROR_MESSAGE);
        }
        return order;
    }

    public Page<Order> findAllByCustomerIdAndCreationDate(OrderFilter orderFilter, Pageable pageable) {
        try {
            return orderRepository.findAllByCustomerIdAndCreationDate(orderFilter, pageable);
        } catch (Exception e) {
            log.error("Error occurred while finding orders by customer id and creation date: [{}]", e.getMessage());
            throw handleInternalServerErrorException(DATABASE_GENERAL_ERROR_MESSAGE);
        }
    }
}
