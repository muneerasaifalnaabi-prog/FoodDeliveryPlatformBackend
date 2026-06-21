package com.fooddelivery.demo.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddress {
    private String street;
    private String city;
    private String building;
    private Boolean isDefault;
    private Customer customer;

}
