package com.fooddelivery.demo.Entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerAddress extends BaseEntity {
    private String street;
    private String city;
    private String building;
    private Boolean isDefault;
    private Customer customer;

}
