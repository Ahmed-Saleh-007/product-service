package com.ejada.product.service.event;

import com.ejada.product.service.event.message.LowStockMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.concurrent.CompletableFuture;

import static com.ejada.product.service.utils.TestUtils.buildLowStockMsg;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(PER_CLASS)
public class ProductLowStockProducerTest {

    @MockitoBean
    private KafkaTemplate<String, LowStockMessage> kafkaTemplate;

    @Autowired
    private ProductLowStockProducer productLowStockProducer;

    @Value("${spring.kafka.topic.product-low-stock:product-low-stock-test}")
    private String productLowStockTopic;

    LowStockMessage message = new LowStockMessage();

    @BeforeEach
    void setup() {
        message = buildLowStockMsg();
    }

    @Test
    void publishLowStockEventSuccess() {

        when(kafkaTemplate.send(eq(productLowStockTopic), eq(message)))
                .thenReturn(CompletableFuture.completedFuture(null));

        productLowStockProducer.publishLowStockEvent(message);

        verify(kafkaTemplate, times(1))
                .send(eq(productLowStockTopic), eq(message));
    }

    @Test
    void publishLowStockEventFailure() {

        doThrow(new RuntimeException("Kafka error")).when(kafkaTemplate).send(anyString(), any());

        productLowStockProducer.publishLowStockEvent(message);

        verify(kafkaTemplate, times(1)).send(productLowStockTopic, message);
    }
}

