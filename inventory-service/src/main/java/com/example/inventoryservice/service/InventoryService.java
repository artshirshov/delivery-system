package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.CountDto;
import com.example.inventoryservice.dto.ErrorKafkaDto;
import com.example.inventoryservice.dto.InventoryDto;
import com.example.inventoryservice.dto.InventoryKafkaDto;
import com.example.inventoryservice.exception.InventoryNotFoundException;
import com.example.inventoryservice.model.Inventory;

public interface InventoryService {

    void completeOrderInventory(InventoryKafkaDto inventoryKafkaDto);

    Inventory createInventory(InventoryDto inventoryDto, Long userId);

    void returnInventory(ErrorKafkaDto errorKafkaDto);

    void replenishInventory(long inventoryId, CountDto countDto) throws InventoryNotFoundException;
}
