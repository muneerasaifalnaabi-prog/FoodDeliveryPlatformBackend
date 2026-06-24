package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.Customer;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("SELECT c FROM Customer c WHERE c.email = :email AND c.isActive = true")
    Optional<Customer> findByEmail(@Param("email") String email);

    @Query("SELECT c FROM Customer c WHERE c.loyaltyPoints >= :points AND c.isActive = true")
    List<Customer> findByLoyaltyPointsGreaterThanEqual(@Param("points") int points);

    @Query(" SELECT c FROM Customer c WHERE c.createdDate BETWEEN :startDate AND :endDate AND c.isActive = true")
    List<Customer> findCustomersRegisteredBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c FROM Customer c WHERE c.isActive = TRUE AND c.id = :customerId")
    Optional<Customer> findCustomerById(@Param("customerId") Integer customerId);






}
