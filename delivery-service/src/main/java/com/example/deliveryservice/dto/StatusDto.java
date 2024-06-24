package com.example.deliveryservice.dto;

import com.example.deliveryservice.dto.enums.ServiceName;
import lombok.Data;
import com.example.deliveryservice.dto.enums.OrderStatus;

@Data
public class StatusDto {

    private OrderStatus status;
    private ServiceName serviceName;
    private String comment;
}
