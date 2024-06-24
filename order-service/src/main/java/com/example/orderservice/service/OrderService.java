package com.example.orderservice.service;

import com.example.orderservice.dto.OrderServiceDto;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.model.Order;
import com.example.orderservice.dto.StatusDto;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {

    Order addOrder(OrderServiceDto orderDto, HttpServletRequest request);

    void updateOrderStatus(Long id, StatusDto statusDto) throws OrderNotFoundException;
}
