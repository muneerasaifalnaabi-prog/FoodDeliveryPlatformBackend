package com.fooddelivery.demo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
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
