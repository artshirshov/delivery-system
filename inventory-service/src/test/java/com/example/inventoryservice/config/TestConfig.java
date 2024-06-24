package com.example.inventoryservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import com.example.inventoryservice.repository.InventoryRepository;
import com.example.inventoryservice.repository.InvoiceInventoryRepository;
import com.example.inventoryservice.repository.InvoiceRepository;
import com.example.inventoryservice.service.InventoryService;
import com.example.inventoryservice.service.InventoryServiceImpl;
import com.example.inventoryservice.service.KafkaService;
import com.example.inventoryservice.service.RequestSendingService;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public KafkaService kafkaService() {
        return mock(KafkaService.class);
    }

    @Bean
    public RequestSendingService requestSendingService() {
        return mock(RequestSendingService.class);
    }

    @Bean
    public InvoiceInventoryRepository invoiceInventoryRepository() {
        return mock(InvoiceInventoryRepository.class);
    }

    @Bean
    public InvoiceRepository invoiceRepository() {
        return mock(InvoiceRepository.class);
    }

    @Bean
    public InventoryRepository inventoryRepository() {
        return mock(InventoryRepository.class);
    }

    @Bean
    public InventoryService inventoryService() {
        return new InventoryServiceImpl(inventoryRepository(), invoiceRepository(), invoiceInventoryRepository(),
                requestSendingService(), kafkaService());
    }
}
