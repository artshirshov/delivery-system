package com.example.orderservice.controller;

import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.orderservice.dto.OrderServiceDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.dto.StatusDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create a new order.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/order")
    public ResponseEntity<Order> addOrder(@Valid @RequestBody OrderServiceDto input, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrder(input, request));
    }

    @Operation(summary = "Update order status by id.", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/order/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable @Parameter(description = "Id of order") long orderId,
                                               @RequestBody StatusDto statusDto) throws OrderNotFoundException {

        orderService.updateOrderStatus(orderId, statusDto);
        return ResponseEntity.ok().build();
    }
}
