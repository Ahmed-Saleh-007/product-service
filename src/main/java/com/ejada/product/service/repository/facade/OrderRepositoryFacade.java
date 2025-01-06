package com.ejada.product.service.repository.facade;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.exception.ErrorCodeEnum;
import com.ejada.product.service.model.entity.Order;
import com.ejada.product.service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
            log.error("Error occurred while creating order OrderRepositoryFacade: [{}]",e.getMessage());
            throw BusinessException.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorCode(ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode())
                    .message(DATABASE_GENERAL_ERROR_MESSAGE)
                    .build();
        }
        return order;
    }

}
