package com.fooddelivery.demo.Controllers;

import com.fooddelivery.demo.Services.ReviewService;
import com.fooddelivery.demo.dto.RequestDTO.ReviewRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.ReviewResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/restaurant/{restaurantId}/customer/{customerId}")
    public ResponseEntity<ReviewResponseDTO> leaveRestaurantReview(@PathVariable Integer restaurantId, @PathVariable Integer customerId, @Valid @RequestBody ReviewRequestDTO dto) {
        return new ResponseEntity<>(reviewService.leaveRestaurantReview(customerId, restaurantId, dto.getRating(), dto.getComment()), HttpStatus.CREATED);
    }

    @PostMapping("/driver/{driverId}/customer/{customerId}")
    public ResponseEntity<ReviewResponseDTO> leaveDriverReview(@PathVariable Integer customerId, @PathVariable Integer driverId, @Valid @RequestBody ReviewRequestDTO dto) {
        return new ResponseEntity<>(reviewService.leaveDriverReview(customerId, driverId, dto.getRating(), dto.getComment()), HttpStatus.CREATED);
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<ReviewResponseDTO>> getDriverReviews(@PathVariable Integer driverId) {
        return ResponseEntity.ok(reviewService.getDriverReviews(driverId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Integer reviewId) {
        return new ResponseEntity<>(reviewService.deleteReview(reviewId), HttpStatus.NO_CONTENT);
    }
    @GetMapping("/restaurant/{restaurantId}/average")
    public ResponseEntity<Double> getRestaurantAverageRating( @PathVariable Integer restaurantId ) {
        return ResponseEntity.ok( reviewService.getRestaurantAverageRating( restaurantId ) );
    }
    @GetMapping("/driver/{driverId}/average")
    public ResponseEntity<Double> getDriverAverageRating( @PathVariable Integer driverId ) {
        return ResponseEntity.ok( reviewService.getDriverAverageRating( driverId ) );
    }
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Page<ReviewResponseDTO>> getRestaurantReviews(@PathVariable Integer restaurantId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size ) {
        return ResponseEntity.ok( reviewService.getRestaurantReviews( restaurantId, page, size ) );
    }



}
