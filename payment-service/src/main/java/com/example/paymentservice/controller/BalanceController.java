package com.example.paymentservice.controller;

import com.example.paymentservice.dto.SumDto;
import com.example.paymentservice.exception.BalanceExistsException;
import com.example.paymentservice.exception.BalanceNotFoundException;
import com.example.paymentservice.model.Balance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.paymentservice.service.BalanceService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class BalanceController {

    private final BalanceService balanceService;

    @Autowired
    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @Operation(summary = "Create a user payment account.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/balance")
    public ResponseEntity<Balance> createPaymentAccount(HttpServletRequest request) throws BalanceExistsException {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(balanceService.createBalance(Long.valueOf(request.getHeader("id"))));
    }

    @Operation(summary = "Replenish user balance.", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/balance/{balanceId}")
    public ResponseEntity<Void> replenishBalance(@PathVariable @Parameter(description = "Id of balance") long balanceId,
                                                 @Valid @RequestBody SumDto sumDto) throws BalanceNotFoundException {

        balanceService.replenishBalance(balanceId, sumDto);
        return ResponseEntity.ok().build();
    }
}
