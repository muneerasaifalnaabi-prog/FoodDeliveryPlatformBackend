package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    @Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId AND o.isActive = true")
    List<Orders> findByCustomerId(@Param("customerId") Integer customerId);

    @Query("SELECT o FROM Orders o WHERE o.restaurant.id = :restaurantId AND o.status = :status AND o.isActive = true")
    List<Orders> findByRestaurantIdAndStatus(@Param("restaurantId") Integer restaurantId, @Param("status") String status);

    @Query("SELECT o FROM Orders o WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.isActive = true")
    List<Orders> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM Orders o WHERE o.restaurant.id = :restaurantId AND o.status = 'DELIVERED' AND o.isActive = true")
    Long countCompletedOrdersForRestaurant(@Param("restaurantId") Integer restaurantId);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Orders o  WHERE DATE(o.orderDate) = :date AND o.status = 'DELIVERED' AND o.isActive = true")
    Double sumDeliveredRevenueByDate(@Param("date") LocalDate date);


    @Query("SELECT o FROM Orders o WHERE o.id = :orderId AND o.isActive = true")
    Optional<Orders> findOrderById(@Param("orderId") Integer orderId);

    @Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId AND o.isActive = true ")
    List<Orders> findOrdersByCustomerId( @Param("customerId") Integer customerId );

    @Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId AND o.status = :status AND o.orderDate BETWEEN :fromDate AND :toDate AND o.isActive = true ")
    Page<Orders> filterCustomerOrders(@Param("customerId") Integer customerId, @Param("status") String status, @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate, Pageable pageable );
    @Query(" SELECT o FROM Orders o WHERE o.restaurant.id = :restaurantId AND o.status = :status AND o.isActive = true ")
    List<Orders> findOrdersByRestaurantAndStatus( @Param("restaurantId") Integer restaurantId, @Param("status") String status );

}
