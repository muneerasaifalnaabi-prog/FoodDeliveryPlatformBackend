package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.Customer;
import com.fooddelivery.demo.Entities.RestaurantLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantLocationRepository extends JpaRepository<RestaurantLocation,Integer> {
    @Query(" SELECT rl FROM RestaurantLocation rl WHERE rl.isActive = true")
    List<RestaurantLocation> findAllActiveLocations();



}
