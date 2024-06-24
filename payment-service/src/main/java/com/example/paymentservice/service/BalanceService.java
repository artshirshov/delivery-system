package com.example.paymentservice.service;

import com.example.paymentservice.dto.SumDto;
import com.example.paymentservice.exception.BalanceExistsException;
import com.example.paymentservice.exception.BalanceNotFoundException;
import com.example.paymentservice.model.Balance;

public interface BalanceService {

    Balance createBalance(Long userId) throws BalanceExistsException;

    void replenishBalance(Long balanceId, SumDto sumDto) throws BalanceNotFoundException;
}
