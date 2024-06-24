package com.example.inventoryservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.example.inventoryservice.config.TestConfig;
import com.example.inventoryservice.dto.ErrorKafkaDto;
import com.example.inventoryservice.dto.InventoryKafkaDto;
import com.example.inventoryservice.dto.OrderDto;
import com.example.inventoryservice.dto.StatusDto;
import com.example.inventoryservice.dto.enums.OrderStatus;
import com.example.inventoryservice.dto.enums.ServiceName;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.model.Invoice;
import com.example.inventoryservice.model.InvoiceInventory;
import com.example.inventoryservice.model.InvoiceInventoryKey;
import com.example.inventoryservice.repository.InventoryRepository;
import com.example.inventoryservice.repository.InvoiceInventoryRepository;
import com.example.inventoryservice.repository.InvoiceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
class InventoryServiceTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceInventoryRepository invoiceInventoryRepository;

    private InventoryKafkaDto inventoryKafkaDto;

    @BeforeEach
    void setUp() {
        OrderDto orderDto = new OrderDto();
        orderDto.setProductId(1L);
        orderDto.setCount(2);
        List<OrderDto> orderDtoList = new ArrayList<>();
        orderDtoList.add(orderDto);

        inventoryKafkaDto = new InventoryKafkaDto();
        inventoryKafkaDto.setOrderDtoList(orderDtoList);
        inventoryKafkaDto.setOrderId(1L);
        inventoryKafkaDto.setUserId(1L);
        inventoryKafkaDto.setDestinationAddress("test address");
        inventoryKafkaDto.setAuthHeaderValue("test header value");
    }

    @AfterEach
    void tearDown() {
        inventoryKafkaDto = null;
    }

    @Test
    void completeOrderInventoryWithoutExceptionTest() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setOrderId(1L);

        Inventory inventory = new Inventory();
        inventory.setId(1L);
        inventory.setCount(30);
        inventory.setTitle("test title");
        inventory.setUserId(1L);
        inventory.setCostPerPiece(2);

        when(invoiceRepository.save(Mockito.any(Invoice.class))).thenReturn(invoice);
        when(inventoryRepository.findInventoryByIdIn(Set.of(1L))).thenReturn(List.of(inventory));
        assertDoesNotThrow(() -> inventoryService.completeOrderInventory(inventoryKafkaDto));
    }

    @Test
    void completeOrderInventoryThrowExceptionTest() {
        Inventory inventory = new Inventory();
        inventory.setId(1L);
        inventory.setCount(30);
        inventory.setTitle("test title");
        inventory.setUserId(1L);
        inventory.setCostPerPiece(2);

        doThrow(new RuntimeException("Undefined test exception")).when(inventoryRepository).save(inventory);
        assertThrows(RuntimeException.class, () -> inventoryService.completeOrderInventory(inventoryKafkaDto));
    }

    @Test
    void returnInventoryThrowExceptionTest() {
        StatusDto statusDto = new StatusDto();
        statusDto.setStatus(OrderStatus.DELIVERY_FAILED);
        statusDto.setServiceName(ServiceName.DELIVERY_SERVICE);
        statusDto.setComment("test comment.");

        ErrorKafkaDto errorKafkaDto = new ErrorKafkaDto();
        errorKafkaDto.setStatusDto(statusDto);
        errorKafkaDto.setOrderId(2L);

        doThrow(new RuntimeException("Undefined exception.")).when(invoiceInventoryRepository).findByInvoice_OrderId(2L);
        assertThrows(RuntimeException.class, () -> inventoryService.returnInventory(errorKafkaDto));
    }

    @Test
    void returnInventoryWithoutExceptionTest() {
        Inventory inventory = new Inventory();
        inventory.setId(1L);
        inventory.setCount(30);
        inventory.setTitle("test title");
        inventory.setUserId(1L);
        inventory.setCostPerPiece(2);

        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setOrderId(1L);

        InvoiceInventoryKey invoiceInventoryKey = new InvoiceInventoryKey();
        invoiceInventoryKey.setInventoryId(1L);
        invoiceInventoryKey.setInvoiceId(1L);

        InvoiceInventory invoiceInventory = new InvoiceInventory();
        invoiceInventory.setInventory(inventory);
        invoiceInventory.setInvoice(invoice);
        invoiceInventory.setInvoiceInventoryKey(invoiceInventoryKey);

        when(invoiceInventoryRepository.findByInvoice_OrderId(1L)).thenReturn(List.of(invoiceInventory));
        assertThrows(RuntimeException.class, () -> inventoryService.completeOrderInventory(inventoryKafkaDto));
    }
}