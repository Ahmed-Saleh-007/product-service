package com.ejada.product.service.service;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.exception.ErrorCodeEnum;
import com.ejada.product.service.model.dto.OrdersResponse;
import com.ejada.product.service.model.entity.Order;
import com.ejada.product.service.model.filter.OrderFilter;
import com.ejada.product.service.repository.facade.OrderRepositoryFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.ejada.product.service.util.Constants.INVALID_DATES_FILTER_ERROR_MESSAGE;
import static com.ejada.product.service.util.Constants.SORT_ORDER_DESC;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepositoryFacade orderRepositoryFacade;
    public OrdersResponse getOrders(OrderFilter orderFilter) {
        log.info("Get orders by filter: [{}]", orderFilter.toString());
        validateOrderFilter(orderFilter);
        Page<Order> orders = orderRepositoryFacade.findAllByCustomerIdAndCreationDate(orderFilter,
                getOrderPageRequest(orderFilter));

        return OrdersResponse.builder()
                .orders(orders.getContent())
                .pageCount(orders.getTotalPages())
                .totalCount(orders.getTotalElements())
                .build();
    }

    private void validateOrderFilter(OrderFilter orderFilter) {
        if(orderFilter.getCreatedAtEnd() != null && orderFilter.getCreatedAtStart() != null &&
        orderFilter.getCreatedAtEnd().isBefore(orderFilter.getCreatedAtStart())){
            throw BusinessException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errorCode(ErrorCodeEnum.BAD_REQUEST.getCode())
                    .message(INVALID_DATES_FILTER_ERROR_MESSAGE)
                    .build();
        }
    }

    private PageRequest getOrderPageRequest(OrderFilter orderFilter) {
        if (StringUtils.hasText(orderFilter.getSortField())) {
            Sort sort = SORT_ORDER_DESC.equalsIgnoreCase(orderFilter.getSortOrder())
                    ? Sort.by(orderFilter.getSortField()).descending()
                    : Sort.by(orderFilter.getSortField()).ascending();
            return PageRequest.of(orderFilter.getPageIndex(), orderFilter.getPageSize(), sort);
        } else {
            return PageRequest.of(orderFilter.getPageIndex(), orderFilter.getPageSize());
        }
    }

}
