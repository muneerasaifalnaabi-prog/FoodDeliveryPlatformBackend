package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
}
