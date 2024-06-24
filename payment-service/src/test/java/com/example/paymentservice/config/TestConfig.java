package com.example.paymentservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import com.example.paymentservice.repository.BalanceRepository;
import com.example.paymentservice.repository.PaymentDetailsRepository;
import com.example.paymentservice.service.KafkaService;
import com.example.paymentservice.service.PaymentService;
import com.example.paymentservice.service.PaymentServiceImpl;
import com.example.paymentservice.service.RequestSendingService;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public KafkaService kafkaServiceMock() {
        return mock(KafkaService.class);
    }

    @Bean
    public BalanceRepository balanceRepositoryMock(){
        return mock(BalanceRepository.class);
    }

    @Bean
    public PaymentDetailsRepository paymentDetailsRepositoryMock() {
        return mock(PaymentDetailsRepository.class);
    }

    @Bean
    public RequestSendingService requestSendingServiceMock() {
        return mock(RequestSendingService.class);
    }

    @Bean
    public PaymentService paymentService() {
        return new PaymentServiceImpl(balanceRepositoryMock(), paymentDetailsRepositoryMock(),
                kafkaServiceMock(), requestSendingServiceMock());
    }
}
