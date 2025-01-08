package com.ejada.product.service.repository.facade;

import com.ejada.product.service.model.entity.Promotion;
import com.ejada.product.service.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.ejada.product.service.exception.CommonExceptionHandler.handleInternalServerErrorException;
import static com.ejada.product.service.util.Constants.DATABASE_GENERAL_ERROR_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromotionRepositoryFacade {

    private final PromotionRepository promotionRepository;

    public Optional<Promotion> findActiveByCode(String code) {
        log.info("Find active promotion by code PromotionRepositoryFacade: [{}]", code);
        try {
            return promotionRepository.findActiveByCode(code, LocalDateTime.now());
        } catch (Exception e) {
            log.error("Error occurred while finding active promotion by code PromotionRepositoryFacade: [{}]", code);
            throw handleInternalServerErrorException(DATABASE_GENERAL_ERROR_MESSAGE);
        }
    }

}
