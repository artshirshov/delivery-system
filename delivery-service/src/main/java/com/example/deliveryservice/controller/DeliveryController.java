package com.example.deliveryservice.controller;

import com.example.deliveryservice.exception.DeliveryNotFoundException;
import com.example.deliveryservice.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Operation(summary = "Remove delivery by id.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delivery/{deliveryId}")
    public ResponseEntity<Void> deleteDeliverById(@PathVariable @Parameter(description = "Id of delivery") long deliveryId)
            throws DeliveryNotFoundException {

        deliveryService.deleteDeliveryById(deliveryId);
        return ResponseEntity.ok().build();
    }
}
