package com.example.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.inventoryservice.model.Inventory;

import java.util.List;
import java.util.Set;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findInventoryByIdIn(Set<Long> inventoryIds);
}
