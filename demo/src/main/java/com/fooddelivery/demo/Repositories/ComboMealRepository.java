package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.ComboMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComboMealRepository extends JpaRepository<ComboMeal, Integer> {
    @Query("SELECT c FROM ComboMeal c JOIN c.menuItems m WHERE m.id = :menuItemId AND c.isActive = true")
    List<ComboMeal> findComboMealsContainingMenuItem(@Param("menuItemId") Integer menuItemId);

    @Query("SELECT c FROM ComboMeal c WHERE c.restaurant.id = :restaurantId AND c.isActive = true")
    List<ComboMeal> findByRestaurantId(@Param("restaurantId") Integer restaurantId);
}
