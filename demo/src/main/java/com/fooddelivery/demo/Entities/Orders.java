package com.fooddelivery.demo.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders extends BaseEntity {
    private String orderCode;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal deliveryFee;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String deliveryNotes;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Restaurant restaurant;
    //@OneToMany()
   // private List<OrderItem> orderItems;
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    @OneToOne()
    private Delivery delivery;
    @OneToOne()
    private Payment payment;

}
