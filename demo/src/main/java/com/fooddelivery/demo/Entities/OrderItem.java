package com.fooddelivery.demo.Entities;

import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

public class OrderItem extends BaseEntity{
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String specialInstructions;
    @ManyToOne
    private Order order;
    @ManyToOne
    private MenuItem menuItem;
}
