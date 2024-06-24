package com.example.inventoryservice.consumer;

import com.example.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.example.inventoryservice.dto.ErrorKafkaDto;
import com.example.inventoryservice.dto.InventoryKafkaDto;

@Component
public class InventoryServiceConsumer {

    private final InventoryService inventoryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryServiceConsumer.class);

    @Autowired
    public InventoryServiceConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "${spring.kafka.inventory-service-topic}")
    public void consumeFromPaymentService(InventoryKafkaDto inventoryKafkaDto) {
        LOGGER.info("Consumed message from Kafka -> '{}'", inventoryKafkaDto);
        inventoryService.completeOrderInventory(inventoryKafkaDto);
    }

    @KafkaListener(topics = "${spring.kafka.error-inventory-service-topic}",
            containerFactory = "kafkaListenerContainerErrorFactory")
    public void consumeFromDeliveryService(ErrorKafkaDto errorKafkaDto) {
        LOGGER.info("Consumed message from Kafka -> '{}'", errorKafkaDto);
        inventoryService.returnInventory(errorKafkaDto);
    }
}
