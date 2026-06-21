package com.fooddelivery.demo.Entities;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CorporateOrder extends BaseEntity {
    private String corporateCode;
    private String companyName;
    private String costCenter;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;
    @ManyToOne
    private Restaurant restaurant;
    @OneToMany()
    private List<OrderItem> corporateOrderItems;

}
