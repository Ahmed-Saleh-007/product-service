package com.ejada.product.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    public List<Objects> getProducts() {
        log.info("Get Products");
        return List.of();
    }

}
