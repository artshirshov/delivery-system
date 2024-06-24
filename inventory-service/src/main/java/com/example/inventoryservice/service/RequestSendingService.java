package com.example.inventoryservice.service;


import com.example.inventoryservice.dto.StatusDto;

public interface RequestSendingService {

    void updateOrderStatusInOrderService(Long orderId, StatusDto statusDto, String authHeaderValue);
}
