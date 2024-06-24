package com.example.deliveryservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.deliveryservice.dto.DeliveryKafkaDto;
import com.example.deliveryservice.dto.ErrorKafkaDto;
import com.example.deliveryservice.dto.OrderKafkaDto;
import com.example.deliveryservice.dto.StatusDto;
import com.example.deliveryservice.dto.enums.OrderStatus;
import com.example.deliveryservice.dto.enums.ServiceName;
import com.example.deliveryservice.exception.DeliveryNotFoundException;
import com.example.deliveryservice.exception.FailedDeliveryException;
import com.example.deliveryservice.model.Delivery;
import com.example.deliveryservice.repository.DeliveryRepository;

import java.util.Optional;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final KafkaService kafkaService;
    private final RequestSendingService requestSendingService;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepository deliveryRepository, KafkaService kafkaService,
                               RequestSendingService requestSendingService) {

        this.deliveryRepository = deliveryRepository;
        this.kafkaService = kafkaService;
        this.requestSendingService = requestSendingService;
    }

    @Transactional
    @Override
    public void makeDelivery(DeliveryKafkaDto deliveryKafkaDto) {
        try {
            Thread.sleep(3000);
            Long orderId = deliveryKafkaDto.getOrderId();
            Long invoiceId = deliveryKafkaDto.getInvoiceId();
            String authHeaderValue = deliveryKafkaDto.getAuthHeaderValue();

            double randomValue = Math.round(Math.random() * 100.0) / 100.0;
            if (randomValue > 0.85) {
                String comment = "The order delivery was unsuccessful.";
                StatusDto statusDto = createStatusDto(OrderStatus.DELIVERY_FAILED, comment);
                ErrorKafkaDto errorKafkaDto = createErrorKafkaDto(orderId, statusDto);
                sendData(orderId, statusDto, authHeaderValue, errorKafkaDto);

                throw new FailedDeliveryException(comment);
            }

            Delivery delivery = new Delivery();
            delivery.setInvoiceId(invoiceId);
            delivery.setDestinationAddress(deliveryKafkaDto.getDestinationAddress());
            deliveryRepository.save(delivery);

            String comment = "The order delivery was successful.";
            StatusDto statusDto = createStatusDto(OrderStatus.DELIVERED, comment);
            OrderKafkaDto orderKafkaDto = createOrderKafkaDto(orderId, statusDto);
            sendData(orderId, statusDto, authHeaderValue, orderKafkaDto);

        } catch (Exception ex) {
            if (!(ex instanceof FailedDeliveryException)) {
                StatusDto statusDto = createStatusDto(OrderStatus.UNEXPECTED_FAILURE, ex.getMessage());
                kafkaService.produce(createErrorKafkaDto(deliveryKafkaDto.getOrderId(), statusDto));
            }

            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void deleteDeliveryById(Long deliveryId) throws DeliveryNotFoundException {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
        if (optionalDelivery.isEmpty()) {
            throw new DeliveryNotFoundException("Delivery with ID " + deliveryId + " not found.");
        }

        deliveryRepository.delete(optionalDelivery.get());
    }

    private StatusDto createStatusDto(OrderStatus orderStatus, String comment) {
        StatusDto statusDto = new StatusDto();
        statusDto.setStatus(orderStatus);
        statusDto.setServiceName(ServiceName.DELIVERY_SERVICE);
        statusDto.setComment(comment);

        return statusDto;
    }

    private OrderKafkaDto createOrderKafkaDto(Long orderId, StatusDto statusDto) {
        OrderKafkaDto orderKafkaDto = new OrderKafkaDto();
        orderKafkaDto.setOrderId(orderId);
        orderKafkaDto.setStatusDto(statusDto);

        return orderKafkaDto;
    }

    private ErrorKafkaDto createErrorKafkaDto(Long orderId, StatusDto statusDto) {
        ErrorKafkaDto errorKafkaDto = new ErrorKafkaDto();
        errorKafkaDto.setOrderId(orderId);
        errorKafkaDto.setStatusDto(statusDto);

        return errorKafkaDto;
    }

    private void sendData(Long orderId, StatusDto statusDto, String authHeaderValue, Object kafkaDto) {
        requestSendingService.updateOrderStatusInOrderService(orderId, statusDto, authHeaderValue);
        kafkaService.produce(kafkaDto);
    }
}
