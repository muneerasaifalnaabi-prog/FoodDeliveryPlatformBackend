package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query(" SELECT p FROM Payment p WHERE p.orders.id = :orderId AND p.isActive = true ")
    Optional<Payment> findPaymentByOrderId( @Param("orderId") Integer orderId );


    @Query("SELECT p FROM Payment p WHERE p.id = :paymentId AND p.isActive = true ")
    Optional<Payment> findPaymentById(@Param("paymentId") Integer paymentId);

    @Query(" SELECT p FROM Payment p WHERE p.isActive = true AND p.paymentMethod = :method AND p.status = :status AND p.processedAt BETWEEN :from AND :to ")
    Page<Payment> filterPayments(@Param("method") String method, @Param("status") String status, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to, Pageable pageable);

    @Query("SELECT p.paymentMethod, SUM(p.amount) FROM Payment p WHERE p.isActive = true GROUP BY p.paymentMethod ")
    List<Object[]> getPaymentAnalyticsByMethod();


}
