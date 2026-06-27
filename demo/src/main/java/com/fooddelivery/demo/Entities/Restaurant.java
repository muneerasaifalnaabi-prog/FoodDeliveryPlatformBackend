package com.fooddelivery.demo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends BaseEntity {
    private String name;
    private String description;
    private String cuisineType;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private BigDecimal minOrderAmount;
    private BigDecimal deliveryFee;
    private Boolean acceptingOrders;

    //add 2 these fields to calcu near restaurant..
    private Double latitude;
    private Double longitude;


    @ManyToOne
    private RestaurantOwner restaurantOwner;
    @OneToMany()
    private List<MenuItem> menuItems;
    @OneToMany()
    private List<ComboMeal> comboMeals;


}
