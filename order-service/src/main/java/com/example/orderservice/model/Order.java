package com.example.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.example.orderservice.model.enums.OrderStatus;
import com.example.orderservice.model.enums.ServiceName;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonIgnore
    private Long userId;

    @JsonIgnore
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @Column(name = "destination_address")
    private String destinationAddress;

    @JsonIgnore
    @Column(name = "cost")
    private Integer cost;

    @CreationTimestamp
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @JsonIgnore
    @UpdateTimestamp
    @Column(name = "modified_time")
    private LocalDateTime modifiedTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @JsonIgnore
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderStatusHistory> orderStatusHistory = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductDetail> productDetails = new ArrayList<>();

    public void addStatusHistory(OrderStatus status, ServiceName serviceName, String comment) {
        getOrderStatusHistory().add(new OrderStatusHistory(null, status, serviceName, comment, this));
    }

    public void addProductDetails(HashMap<Long, Integer> quantityProducts) {
        List<ProductDetail> productDetailList = new ArrayList<>();
        quantityProducts.forEach((key, value) ->
                productDetailList.add(new ProductDetail(null, key, value, this)));

        getProductDetails().addAll(productDetailList);
    }
}
