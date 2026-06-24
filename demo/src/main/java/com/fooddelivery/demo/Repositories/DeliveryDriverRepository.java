package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.DeliveryDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriver, Integer> {
    @Query("SELECT d FROM DeliveryDriver d WHERE d.id = :driverId AND d.isActive = true ")
    Optional<DeliveryDriver> findDriverById(@Param("driverId") Integer driverId );

    @Query(" SELECT d FROM DeliveryDriver d WHERE d.isOnline = true AND d.isActive = true ")
    Optional<DeliveryDriver> findFirstAvailableOnlineDriver();
}
