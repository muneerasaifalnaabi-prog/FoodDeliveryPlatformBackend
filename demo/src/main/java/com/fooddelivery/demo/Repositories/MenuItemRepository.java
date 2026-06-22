package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
}
