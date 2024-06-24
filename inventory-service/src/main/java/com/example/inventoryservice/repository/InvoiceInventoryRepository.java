package com.example.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.inventoryservice.model.InvoiceInventory;
import com.example.inventoryservice.model.InvoiceInventoryKey;

import java.util.List;

@Repository
public interface InvoiceInventoryRepository extends JpaRepository<InvoiceInventory, InvoiceInventoryKey> {

    List<InvoiceInventory> findByInvoice_OrderId(Long orderId);
}
