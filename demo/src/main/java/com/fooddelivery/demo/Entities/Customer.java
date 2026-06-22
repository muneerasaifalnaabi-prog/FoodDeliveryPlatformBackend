package com.fooddelivery.demo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer extends Person {
    private Integer loyaltyPoints;
    private String customerCode;
    @OneToMany()
    private List<CustomerAddress> addresses;
    @OneToMany()
    private List<Orders> orders;
    @OneToMany()
    private List<Review> reviews;

}
