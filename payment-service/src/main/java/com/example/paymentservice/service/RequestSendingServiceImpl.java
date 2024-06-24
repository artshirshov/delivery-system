package com.example.paymentservice.service;

import com.example.paymentservice.dto.StatusDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RequestSendingServiceImpl implements RequestSendingService {

    @Value("${order-service-url}")
    private String urlOrderService;

    @Override
    public void updateOrderStatusInOrderService(Long orderId, StatusDto statusDto, String authHeaderValue) {
        WebClient.create(urlOrderService)
                .patch()
                .uri("/order/" + orderId)
                .header("Authorization", authHeaderValue)
                .body(BodyInserters.fromValue(statusDto))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
