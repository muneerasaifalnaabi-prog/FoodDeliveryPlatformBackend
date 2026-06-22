package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Integer> {
    @Query(" SELECT ca FROM CustomerAddress ca WHERE LOWER(ca.city) = LOWER(:city) AND ca.isActive = true")
    List<CustomerAddress> findByCity(@Param("city") String city);

    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.customer.id = :customerId AND ca.isActive = true")
    List<CustomerAddress> findByCustomerId(@Param("customerId") Integer customerId);
}
