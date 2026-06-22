package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<CustomerAddress, Integer> {
}
