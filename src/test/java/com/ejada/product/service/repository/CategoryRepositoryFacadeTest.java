package com.ejada.product.service.repository;

import com.ejada.product.service.exception.BusinessException;
import com.ejada.product.service.model.entity.Category;
import com.ejada.product.service.repository.facade.CategoryRepositoryFacade;
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
class CategoryRepositoryFacadeTest {

    @Autowired
    private CategoryRepositoryFacade categoryRepositoryFacade;

    @MockitoBean
    private CategoryRepository categoryRepository;

    @Test
    void testFindByIdSuccess() {
        when(categoryRepository.findById(any())).thenReturn(Optional.of(new Category()));
        assertDoesNotThrow(() -> categoryRepositoryFacade.findById(1));
    }

    @Test
    void testFindByIdFailsWithSqlException() {
        doAnswer(
                invocation -> {
                    throw new SQLException("");
                })
                .when(categoryRepository)
                .findById(any());
        assertThrows(BusinessException.class, () -> categoryRepositoryFacade.findById(1));
    }

}
