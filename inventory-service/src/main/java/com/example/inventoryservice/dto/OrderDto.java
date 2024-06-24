package com.example.inventoryservice.dto;

import lombok.Data;

@Data
public class OrderDto {

    private Long productId;
    private Integer count;
}
