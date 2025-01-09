package com.ejada.product.service.repository;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.model.entity.Customer;
import com.ejada.product.service.repository.facade.CustomerRepositoryFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(PER_CLASS)
class CustomerRepositoryFacadeTest {

    @Autowired
    private CustomerRepositoryFacade customerRepositoryFacade;

    @MockitoBean
    private CustomerRepository customerRepository;

    @Test
    void testFindByIdSuccess() {
        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
        assertDoesNotThrow(() -> customerRepositoryFacade.findById(1));
    }

    @Test
    void testFindByIdFailsWithSqlException() {
        doAnswer(
                invocation -> {
                    throw new SQLException("");
                })
                .when(customerRepository)
                .findById(any());
        assertThrows(BusinessException.class, () -> customerRepositoryFacade.findById(1));
    }

}
