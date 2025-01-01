package com.ejada.product.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    public List<Objects> getOrders() {
        log.info("Get Orders");
        return List.of();
    }

}
