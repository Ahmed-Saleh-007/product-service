package com.ejada.product.service.repository;

import com.ejada.product.service.model.entity.Promotion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PromotionRepository extends CrudRepository<Promotion, Integer> {

    @Query("""
            SELECT promotion FROM Promotion promotion
            WHERE promotion.code = :code
            AND promotion.active = true
            AND promotion.startDate <= :now
            AND promotion.endDate >= :now
            """)
    Optional<Promotion> findActiveByCode(String code, LocalDateTime now);

}
