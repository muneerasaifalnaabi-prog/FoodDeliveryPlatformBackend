package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r WHERE r.id = :reviewId AND r.isActive = true ")
    Optional<Review> findReviewById(@Param("reviewId") Integer reviewId);

    @Query("SELECT r FROM Review r WHERE r.deliveryDriver.id = :driverId AND r.isActive = true ")
    List<Review> findReviewsByDriverId(@Param("driverId") Integer driverId);

    @Query(" SELECT AVG(r.rating)FROM Review r WHERE r.restaurant.id = :restaurantId AND r.isActive = true")
    Double getRestaurantAverageRating(@Param("restaurantId") Integer restaurantId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.deliveryDriver.id = :driverId AND r.isActive = true ")
    Double getDriverAverageRating(@Param("driverId") Integer driverId);

    @Query(" SELECT r FROM Review r WHERE r.restaurant.id = :restaurantId AND r.isActive = true ")
    Page<Review> findRestaurantReviews(@Param("restaurantId") Integer restaurantId, Pageable pageable);

}
