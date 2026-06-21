package com.fooddelivery.demo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantOwner extends Person {
    private String businessLicenseCode;
    @OneToMany()
    private List<Restaurant> restaurants;
}
