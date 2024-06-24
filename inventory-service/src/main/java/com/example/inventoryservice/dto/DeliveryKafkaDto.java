package com.example.inventoryservice.dto;

import lombok.Data;

@Data
public class DeliveryKafkaDto {

    private Long invoiceId;
    private Long orderId;
    private String destinationAddress;
    private String authHeaderValue;
}