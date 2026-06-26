package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query(" SELECT p FROM Payment p WHERE p.orders.id = :orderId AND p.isActive = true ")
    Optional<Payment> findPaymentByOrderId(@Param("orderId") Integer orderId );

    @Query("SELECT p FROM Payment p WHERE p.id = :paymentId AND p.isActive = true ")
    Optional<Payment> findPaymentById( @Param("paymentId") Integer paymentId );


}
