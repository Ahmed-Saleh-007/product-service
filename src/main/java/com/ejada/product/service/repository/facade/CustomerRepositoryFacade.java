package com.ejada.product.service.repository.facade;

import com.ejada.product.service.model.entity.Customer;
import com.ejada.product.service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ejada.product.service.exception.CommonExceptionHandler.handleInternalServerErrorException;
import static com.ejada.product.service.util.Constants.DATABASE_GENERAL_ERROR_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerRepositoryFacade {

    private final CustomerRepository customerRepository;

    public Optional<Customer> findById(Integer id) {
        log.info("Find customer by id CustomerRepositoryFacade: [{}]", id);
        try {
            return customerRepository.findById(id);
        } catch (Exception e) {
            log.error("Error occurred while finding customer by id CustomerRepositoryFacade: [{}]", id);
            throw handleInternalServerErrorException(DATABASE_GENERAL_ERROR_MESSAGE);
        }
    }

}
