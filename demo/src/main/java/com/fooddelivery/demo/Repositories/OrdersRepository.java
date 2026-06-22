package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
}
