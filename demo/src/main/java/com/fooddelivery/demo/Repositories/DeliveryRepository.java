package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

}
