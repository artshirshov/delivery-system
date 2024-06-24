package com.example.deliveryservice.service;

import com.example.deliveryservice.dto.StatusDto;

public interface RequestSendingService {

    void updateOrderStatusInOrderService(Long orderId, StatusDto statusDto, String authHeaderValue);
}
