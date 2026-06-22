package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.ComboMeal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComboMealRepository extends JpaRepository<ComboMeal, Integer> {
}
