package com.example.orderservice.dto;

import com.example.orderservice.model.enums.OrderStatus;
import com.example.orderservice.model.enums.ServiceName;
import lombok.Data;

@Data
public class StatusDto {

    private OrderStatus status;
    private ServiceName serviceName;
    private String comment;
}
