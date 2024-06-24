package com.example.orderservice.consumer;

import com.example.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.example.orderservice.dto.ErrorKafkaDto;
import com.example.orderservice.dto.OrderKafkaDto;
import com.example.orderservice.exception.OrderNotFoundException;

@Component
public class OrderServiceConsumer {

    private final OrderService orderService;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceConsumer.class);

    @Autowired
    public OrderServiceConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "${spring.kafka.order-service-topic}",
            containerFactory = "kafkaListenerContainerOrderKafkaDtoFactory")
    private void consumeFromDeliveryService(OrderKafkaDto orderKafkaDto) {
        try {
            LOGGER.info("Consumed message from Kafka -> '{}'", orderKafkaDto);
            orderService.updateOrderStatus(orderKafkaDto.getOrderId(), orderKafkaDto.getStatusDto());
        } catch (OrderNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${spring.kafka.error-order-service-topic}")
    public void consumeFromPaymentService(ErrorKafkaDto errorKafkaDto) {
        try {
            LOGGER.info("Consumed message from Kafka -> '{}'", errorKafkaDto);
            orderService.updateOrderStatus(errorKafkaDto.getOrderId(), errorKafkaDto.getStatusDto());
        } catch (OrderNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
