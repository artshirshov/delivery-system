package com.example.paymentservice.service;

import com.example.paymentservice.dto.StatusDto;

public interface RequestSendingService {

    void updateOrderStatusInOrderService(Long orderId, StatusDto statusDto, String authHeaderValue);
}
