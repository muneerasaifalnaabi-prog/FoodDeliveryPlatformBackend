package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.RestaurantLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantLocationRepository extends JpaRepository<RestaurantLocation,Integer> {

}
