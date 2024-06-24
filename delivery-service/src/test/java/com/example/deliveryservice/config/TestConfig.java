package com.example.deliveryservice.config;

import com.example.deliveryservice.repository.DeliveryRepository;
import com.example.deliveryservice.service.DeliveryService;
import com.example.deliveryservice.service.DeliveryServiceImpl;
import com.example.deliveryservice.service.KafkaService;
import com.example.deliveryservice.service.RequestSendingService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public RequestSendingService requestSendingService() {
        return mock(RequestSendingService.class);
    }

    @Bean
    public KafkaService kafkaService() {
        return mock(KafkaService.class);
    }

    @Bean
    public DeliveryRepository deliveryRepository() {
        return mock(DeliveryRepository.class);
    }

    @Bean
    public DeliveryService deliveryService() {
        return new DeliveryServiceImpl(deliveryRepository(), kafkaService(), requestSendingService());
    }
}
