package com.fooddelivery.demo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {
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
    @OneToMany()
    private List<OrderItem> orderItems;
    @OneToOne()
    private Delivery delivery;
    @OneToOne()
    private Payment payment;

}
