package com.example.deliveryservice.dto;

import lombok.Data;

@Data
public class OrderKafkaDto {

    private Long orderId;
    private StatusDto statusDto;
}
