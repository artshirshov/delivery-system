package com.example.deliveryservice.dto;

import lombok.Data;

@Data
public class ErrorKafkaDto {

    private Long orderId;
    private StatusDto statusDto;
}
