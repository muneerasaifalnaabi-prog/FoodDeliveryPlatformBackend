package com.fooddelivery.demo.Entities;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.util.List;

public class ComboMeal extends BaseEntity {
    private String comboName;
    private String description;
    private BigDecimal totalPrice;
    private Boolean isAvailable;
    @ManyToOne
    private Restaurant restaurant;
    @ManyToMany
    private List<MenuItem> menuItems;
}
