package com.fooddelivery.demo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem extends BaseEntity{
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean isAvailable;
    private Boolean isVegetarian;
    private Integer calories;
    @ManyToOne
    private Restaurant restaurant;
    @OneToMany()
    private List<OrderItem> orderItems;

}
