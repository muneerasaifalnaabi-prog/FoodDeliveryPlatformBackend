package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.MenuItem;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.isActive = true")
    List<MenuItem> findByRestaurantId(@Param("restaurantId") Integer restaurantId);

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.isAvailable = true AND m.isActive = true")
    List<MenuItem> findByRestaurantIdAndIsAvailableTrue(@Param("restaurantId") Integer restaurantId);

    @Query("SELECT m FROM MenuItem m WHERE m.isVegetarian = true AND m.isActive = true ")
    List<MenuItem> findByIsVegetarianTrue();

    @Query(" SELECT m FROM MenuItem m WHERE m.price BETWEEN :minPrice AND :maxPrice AND m.isActive = true")
    List<MenuItem> findByPriceBetween(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);

    @Query("SELECT m FROM MenuItem m WHERE m.id = :menuItemId AND m.isActive = true")
    Optional<MenuItem> findMenuItemById(@Param("menuItemId") Integer menuItemId);

    @Query(value = " SELECT mi.* FROM menu_item mi JOIN order_item oi ON mi.id = oi.menu_item_id WHERE mi.restaurant_id = :restaurantId AND mi.is_active = true GROUP BY mi.id ORDER BY SUM(oi.quantity) DESC ", nativeQuery = true)
    List<MenuItem> findTopSellingMenuItems( @Param("restaurantId") Integer restaurantId );

    @Query(" SELECT m FROM MenuItem m WHERE m.isActive = true AND LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND m.calories BETWEEN :minCalories AND :maxCalories ")
    List<MenuItem> searchMenuItems( @Param("keyword") String keyword, @Param("minCalories") Integer minCalories, @Param("maxCalories") Integer maxCalories );



}