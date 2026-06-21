package com.fooddelivery.demo.Entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer extends BaseEntity {
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String phone;
    private String passwordHash;
    private Integer loyaltyPoints;
    private String customerCode;
    @OneToMany()
    private List<CustomerAddress> addresses;
    @OneToMany()
    private List<Order> orders ;
    @OneToMany()
    private List<Review> reviews ;

}
