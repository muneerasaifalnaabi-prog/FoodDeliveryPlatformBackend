package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.cuisineType) = LOWER(:cuisineType)AND r.isActive = true ")
    List<Restaurant> findByCuisineTypeIgnoreCase(@Param("cuisineType") String cuisineType);

    @Query("SELECT r FROM Restaurant r WHERE r.acceptingOrders = acceptingOrders AND r.isActive = true")
    List<Restaurant> findByAcceptingOrdersTrue();

    @Query("SELECT r FROM Restaurant r WHERE r.deliveryFee <= :deliveryFee AND r.isActive = true")
    List<Restaurant> findByDeliveryFeeLessThanEqual(@Param("fee") double fee);

    @Query("SELECT r FROM Restaurant r WHERE r.restaurantOwner.id = :ownerId AND r.isActive = true")
    List<Restaurant> findRestaurantsByOwnerId(@Param("ownerId") Integer ownerId);

    @Query(" SELECT r FROM Restaurant r  WHERE LOWER(r.name)  LIKE LOWER(CONCAT('%', :keyword, '%')) AND r.isActive = true")
    List<Restaurant> searchRestaurantsByKeyword(@Param("keyword") String keyword);

    @Query("SELECT r FROM Restaurant r WHERE r.isActive = TRUE AND r.id = :restaurantId")
    Optional<Restaurant> findRestaurantById(@Param("restaurantId") Integer restaurantId);
}
