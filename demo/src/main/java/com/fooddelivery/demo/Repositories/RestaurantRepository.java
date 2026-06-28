package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.ComboMeal;
import com.fooddelivery.demo.Entities.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.cuisineType) = LOWER(:cuisineType) AND r.isActive = true ")
    List<Restaurant> findByCuisineTypeIgnoreCase(@Param("cuisineType") String cuisineType);

    @Query(" SELECT r FROM Restaurant r WHERE r.acceptingOrders = true AND r.isActive = true")
    List<Restaurant> findByAcceptingOrdersTrue();

    @Query(" SELECT r FROM Restaurant r WHERE r.deliveryFee <= :fee AND r.isActive = true ")
    List<Restaurant> findByDeliveryFeeLessThanEqual(@Param("fee") double fee);

    @Query("SELECT r FROM Restaurant r WHERE r.restaurantOwner.id = :ownerId AND r.isActive = true")
    List<Restaurant> findRestaurantsByOwnerId(@Param("ownerId") Integer ownerId);

    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))AND r.isActive = true")
    Page<Restaurant> searchRestaurantsByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT r FROM Restaurant r WHERE r.isActive = TRUE AND r.id = :restaurantId")
    Optional<Restaurant> findRestaurantById(@Param("restaurantId") Integer restaurantId);

    @Query("SELECT r FROM Restaurant r WHERE r.isActive = true")
    List<Restaurant> getAllRestaurant();

    @Query(" SELECT c FROM ComboMeal c WHERE c.restaurant.id = :restaurantId AND c.isActive = true ")
    List<ComboMeal> findCombosByRestaurantId(@Param("restaurantId") Integer restaurantId);

    @Query(" SELECT COALESCE(SUM(o.totalAmount), 0) FROM Orders o WHERE o.restaurant.id = :restaurantId AND o.createdDate BETWEEN :fromDate AND :toDate AND o.status = 'DELIVERED' AND o.isActive = true ")
    Double getRestaurantRevenueBetweenDates(@Param("restaurantId") Integer restaurantId, @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate );

}
