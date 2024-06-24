package com.example.deliveryservice.service;

import com.example.deliveryservice.dto.DeliveryKafkaDto;
import com.example.deliveryservice.exception.DeliveryNotFoundException;

public interface DeliveryService {

    void makeDelivery(DeliveryKafkaDto deliveryKafkaDto);

    void deleteDeliveryById(Long deliveryId) throws DeliveryNotFoundException;
}
