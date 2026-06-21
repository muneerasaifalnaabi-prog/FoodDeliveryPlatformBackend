package com.fooddelivery.demo.Entities;


import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

import java.util.List;

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

}
