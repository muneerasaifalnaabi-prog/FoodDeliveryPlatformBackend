package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewRequestDTO {

    @NotNull(message = "Customer id is required")
    private Integer customerId;

    private Integer restaurantId;

    private Integer driverId;

    @NotBlank(message = "Target type is required")
    private String targetType;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;


    private String comment;

    public Review toEntity() {
        Review review = new Review();

        applyTo(review);
        return review;
    }

    public void applyTo(Review review) {

        review.setTargetType(targetType);
        review.setRating(rating);
        review.setComment(comment);
    }
}