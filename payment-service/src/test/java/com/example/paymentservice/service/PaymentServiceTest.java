package com.example.paymentservice.service;

import com.example.paymentservice.config.TestConfig;
import com.example.paymentservice.dto.ErrorKafkaDto;
import com.example.paymentservice.dto.OrderDto;
import com.example.paymentservice.dto.PaymentKafkaDto;
import com.example.paymentservice.dto.StatusDto;
import com.example.paymentservice.dto.enums.OrderStatus;
import com.example.paymentservice.dto.enums.ServiceName;
import com.example.paymentservice.model.Balance;
import com.example.paymentservice.model.PaymentDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.example.paymentservice.repository.BalanceRepository;
import com.example.paymentservice.repository.PaymentDetailsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Test
    void payTestWithoutException() {
        OrderDto orderDto = new OrderDto();
        orderDto.setProductId(1L);
        orderDto.setCount(2);
        List<OrderDto> orderDtoList = new ArrayList<>();
        orderDtoList.add(orderDto);

        PaymentKafkaDto paymentKafkaDto = new PaymentKafkaDto();
        paymentKafkaDto.setUserId(1L);
        paymentKafkaDto.setOrderDtoList(orderDtoList);
        paymentKafkaDto.setCost(100);
        paymentKafkaDto.setOrderId(1L);
        paymentKafkaDto.setAuthHeaderValue("test header value.");
        paymentKafkaDto.setDestinationAddress("test address");

        Balance balance = new Balance();
        balance.setBalance(1000);
        balance.setUserId(1L);

        when(balanceRepository.findBalanceByUserId(1L)).thenReturn(Optional.of(balance));
        assertDoesNotThrow(() -> paymentService.pay(paymentKafkaDto));
    }

    @Test
    void payTestThrowException() {
        OrderDto orderDto = new OrderDto();
        orderDto.setProductId(1L);
        orderDto.setCount(2);
        List<OrderDto> orderDtoList = new ArrayList<>();
        orderDtoList.add(orderDto);

        PaymentKafkaDto paymentKafkaDto = new PaymentKafkaDto();
        paymentKafkaDto.setUserId(1L);
        paymentKafkaDto.setOrderDtoList(orderDtoList);
        paymentKafkaDto.setCost(100);
        paymentKafkaDto.setOrderId(1L);
        paymentKafkaDto.setAuthHeaderValue("test header value.");
        paymentKafkaDto.setDestinationAddress("test address");

        when(balanceRepository.findBalanceByUserId(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> paymentService.pay(paymentKafkaDto));
    }

    @Test
    void resetPaymentThrowException() {
        StatusDto statusDto = new StatusDto();
        statusDto.setComment("test comment.");
        statusDto.setStatus(OrderStatus.INVENTMENT_FAILED);
        statusDto.setServiceName(ServiceName.INVENTORY_SERVICE);

        ErrorKafkaDto errorKafkaDto = new ErrorKafkaDto();
        errorKafkaDto.setOrderId(1L);
        errorKafkaDto.setStatusDto(statusDto);

        when(paymentDetailsRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> paymentService.resetPayment(errorKafkaDto));
    }

    @Test
    void resetPaymentWithoutException() {
        StatusDto statusDto = new StatusDto();
        statusDto.setComment("test comment.");
        statusDto.setStatus(OrderStatus.INVENTMENT_FAILED);
        statusDto.setServiceName(ServiceName.INVENTORY_SERVICE);

        ErrorKafkaDto errorKafkaDto = new ErrorKafkaDto();
        errorKafkaDto.setOrderId(1L);
        errorKafkaDto.setStatusDto(statusDto);

        Balance balance = new Balance();
        balance.setUserId(1L);
        balance.setBalance(1000);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setOrderId(1L);
        paymentDetails.setSum(20);
        paymentDetails.setBalance(balance);

        when(paymentDetailsRepository.findByOrderId(1L)).thenReturn(Optional.of(paymentDetails));
        assertDoesNotThrow(() -> paymentService.resetPayment(errorKafkaDto));
    }
}