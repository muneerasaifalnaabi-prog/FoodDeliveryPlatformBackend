package com.fooddelivery.demo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RestaurantLocation extends BaseEntity{
    private Double latitude;
    private Double longitude;
    @ManyToOne
    private Restaurant restaurant;
}
