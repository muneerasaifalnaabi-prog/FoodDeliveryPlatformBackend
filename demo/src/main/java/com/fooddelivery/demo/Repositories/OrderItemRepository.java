package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {

    @Query("SELECT oi FROM OrderItem oi WHERE oi.isActive = TRUE AND oi.id = :orderItemId")
    Optional<OrderItem> findOrderItemById(@Param("orderItemId") Integer orderItemId);
}

