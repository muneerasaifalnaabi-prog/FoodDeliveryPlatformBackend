package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.Customer;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("SELECT c FROM Customer c WHERE c.email = :email AND c.isActive = true")
    List<Customer> findByEmail(@Param("email") String email);



}
