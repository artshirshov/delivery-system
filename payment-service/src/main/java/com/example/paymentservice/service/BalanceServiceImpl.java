package com.example.paymentservice.service;

import com.example.paymentservice.dto.SumDto;
import com.example.paymentservice.exception.BalanceExistsException;
import com.example.paymentservice.exception.BalanceNotFoundException;
import com.example.paymentservice.model.Balance;
import com.example.paymentservice.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;

    @Autowired
    public BalanceServiceImpl(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Override
    public Balance createBalance(Long userId) throws BalanceExistsException {
        Optional<Balance> optionalBalance = balanceRepository.findBalanceByUserId(userId);
        if (optionalBalance.isPresent()) {
            throw new BalanceExistsException("A balance account for the specified user already exists.");
        }

        Balance balance = new Balance();
        balance.setUserId(userId);
        balance.setBalance(0);
        return balanceRepository.save(balance);
    }

    @Override
    public void replenishBalance(Long balanceId, SumDto sumDto) throws BalanceNotFoundException {
        Optional<Balance> optionalBalance = balanceRepository.findById(balanceId);
        if (optionalBalance.isEmpty()) {
            throw new BalanceNotFoundException("The balance with the specified ID was not found.");
        }

        Balance balance = optionalBalance.get();
        balance.setBalance(balance.getBalance() + sumDto.getSum());
        balanceRepository.save(balance);
    }
}
