package com.ejada.product.service.repository;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.repository.facade.PromotionRepositoryFacade;
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
class PromotionRepositoryFacadeTest {

    @Autowired
    private PromotionRepositoryFacade promotionRepositoryFacade;

    @MockitoBean
    private PromotionRepository promotionRepository;

    @Test
    void testGetActivePromotionByCodeSuccess() {
        when(promotionRepository.findActiveByCode(any(), any()))
                .thenReturn(Optional.empty());
        assertDoesNotThrow(() -> promotionRepositoryFacade.findActiveByCode("code"));
    }

    @Test
    void testGetActivePromotionByCodeWithSqlException() {
        doAnswer(
                invocation -> {
                    throw new SQLException("");
                })
                .when(promotionRepository)
                .findActiveByCode(any(), any());
        assertThrows(BusinessException.class, () ->
                promotionRepositoryFacade.findActiveByCode("code"));
    }

}
