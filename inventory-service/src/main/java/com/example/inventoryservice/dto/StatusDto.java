package com.example.inventoryservice.dto;

import com.example.inventoryservice.dto.enums.OrderStatus;
import com.example.inventoryservice.dto.enums.ServiceName;
import lombok.Data;

@Data
public class StatusDto {

    private OrderStatus status;
    private ServiceName serviceName;
    private String comment;
}
