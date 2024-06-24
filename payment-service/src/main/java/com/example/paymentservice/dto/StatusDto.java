package com.example.paymentservice.dto;

import com.example.paymentservice.dto.enums.OrderStatus;
import com.example.paymentservice.dto.enums.ServiceName;
import lombok.Data;

@Data
public class StatusDto {

    private OrderStatus status;
    private ServiceName serviceName;
    private String comment;
}
