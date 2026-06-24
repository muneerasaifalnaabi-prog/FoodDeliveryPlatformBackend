package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {

    Optional<Object> findOrderItemById(Integer orderItemId);
}
