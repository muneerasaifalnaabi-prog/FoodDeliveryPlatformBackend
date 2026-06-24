package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.RestaurantOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RestaurantOwnerRepository extends JpaRepository<RestaurantOwner,Integer> {
    @Query("SELECT ro FROM RestaurantOwner ro WHERE ro.isActive = TRUE AND ro.id = :ownerId")
    Optional<RestaurantOwner> findActiveById(@Param("ownerId") Integer ownerId);
}

