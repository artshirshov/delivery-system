package com.example.paymentservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaymentKafkaDto {

    private Long userId;
    private List<OrderDto> orderDtoList;
    private Integer cost;
    private Long orderId;
    private String destinationAddress;
    private String authHeaderValue;
}
