package com.fooddelivery.demo.Entities;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;

public class Delivery extends BaseEntity {
    private String trackingCode;
    private String status;
    private LocalDateTime assignedAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;
    @OneToOne
    private Order order;
    @ManyToOne
    private DeliveryDriver deliveryDriver;
}
