package com.ejada.product.service.repository.facade;

import com.ejada.product.service.model.entity.Category;
import com.ejada.product.service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ejada.product.service.exception.CommonExceptionHandler.handleInternalServerErrorException;
import static com.ejada.product.service.util.Constants.DATABASE_GENERAL_ERROR_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryRepositoryFacade {
    private final CategoryRepository categoryRepository;

    public Optional<Category> findById(Integer categoryId) {
        try {
            return categoryRepository.findById(categoryId);
        } catch (Exception e) {
            log.error("Error occurred while finding category by ID CategoryRepositoryFacade: [{}]", categoryId);
            throw handleInternalServerErrorException(DATABASE_GENERAL_ERROR_MESSAGE);
        }

    }

}
