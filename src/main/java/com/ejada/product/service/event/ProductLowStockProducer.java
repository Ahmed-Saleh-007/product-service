package com.ejada.product.service.event;

import com.ejada.product.service.event.message.LowStockMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductLowStockProducer {
    private final KafkaTemplate<String, LowStockMessage> kafkaTemplate;

    @Value("${spring.kafka.topic.product-low-stock}")
    private String productLowStockTopic;

    public void publishLowStockEvent(LowStockMessage message) {
        try {
            log.info("Publishing low stock event: {}", message);
            kafkaTemplate.send(productLowStockTopic, message);
        }
        catch (Exception e) {
            log.error("Failed to publish low stock event for product ID: {}. Error: {}", message.getProductId(), e.getMessage());
        }

    }
}
