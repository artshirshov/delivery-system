package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderServiceDto;
import com.example.orderservice.dto.PaymentKafkaDto;
import com.example.orderservice.dto.StatusDto;
import com.example.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.dto.*;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.enums.OrderStatus;
import com.example.orderservice.model.enums.ServiceName;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaService kafkaService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, KafkaService kafkaService) {
        this.orderRepository = orderRepository;
        this.kafkaService = kafkaService;
    }

    @Transactional
    @Override
    public Order addOrder(OrderServiceDto orderServiceDto, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getHeader("id"));
        String authHeaderValue = request.getHeader("Authorization");
        List<OrderDto> initialOrderDtoList = orderServiceDto.getOrderDtoList();
        Integer cost = orderServiceDto.getCost();
        String destinationAddress = orderServiceDto.getDestinationAddress();

        HashMap<Long, Integer> quantityProducts = new HashMap<>();
        List<OrderDto> orderDtoList = getOrderDtoListWithoutDuplicates(initialOrderDtoList, quantityProducts);

        Order newOrder = new Order();
        newOrder.setDestinationAddress(orderServiceDto.getDestinationAddress());
        newOrder.setCost(orderServiceDto.getCost());
        newOrder.setUserId(userId);
        newOrder.addProductDetails(quantityProducts);
        newOrder.setStatus(OrderStatus.REGISTERED);
        newOrder.addStatusHistory(newOrder.getStatus(), ServiceName.ORDER_SERVICE, "Order created");
        Order savedOrder = orderRepository.save(newOrder);

        PaymentKafkaDto paymentKafkaDto = createPaymentKafkaDto(userId, orderDtoList, cost,
                savedOrder.getId(), authHeaderValue, destinationAddress);
        kafkaService.produce(paymentKafkaDto);

        return savedOrder;
    }

    private List<OrderDto> getOrderDtoListWithoutDuplicates(List<OrderDto> orderDtoList,
                                                            HashMap<Long, Integer> quantityProducts) {

        orderDtoList.forEach(orderDto ->
                quantityProducts.merge(orderDto.getProductId(), orderDto.getCount(), Integer::sum));
        orderDtoList.clear();
        quantityProducts.forEach((key, value) -> orderDtoList.add(createOrderDto(key, value)));

        return orderDtoList;
    }

    private OrderDto createOrderDto(Long productId, Integer count) {
        OrderDto orderDto = new OrderDto();
        orderDto.setProductId(productId);
        orderDto.setCount(count);

        return orderDto;
    }

    private PaymentKafkaDto createPaymentKafkaDto(Long userId, List<OrderDto> orderDtoList, Integer cost,
                                                  Long orderId, String authHeaderValue, String destinationAddress) {

        PaymentKafkaDto paymentKafkaDto = new PaymentKafkaDto();
        paymentKafkaDto.setUserId(userId);
        paymentKafkaDto.setOrderDtoList(orderDtoList);
        paymentKafkaDto.setCost(cost);
        paymentKafkaDto.setOrderId(orderId);
        paymentKafkaDto.setAuthHeaderValue(authHeaderValue);
        paymentKafkaDto.setDestinationAddress(destinationAddress);

        return paymentKafkaDto;
    }

    @Transactional
    @Override
    public void updateOrderStatus(Long id, StatusDto statusDto) throws OrderNotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(id).stream().findFirst();
        if (orderOptional.isEmpty()) {
            throw new OrderNotFoundException(id);
        }

        Order order = orderOptional.get();
        if (order.getStatus() == statusDto.getStatus()) {
            log.info("Request with same status {} for order {} from service {}", statusDto.getStatus(),
                    id, statusDto.getServiceName());
            return;
        }
        order.setStatus(statusDto.getStatus());
        order.addStatusHistory(statusDto.getStatus(), statusDto.getServiceName(), statusDto.getComment());
        orderRepository.save(order);
    }
}
