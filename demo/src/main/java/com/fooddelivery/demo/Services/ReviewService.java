package com.fooddelivery.demo.Services;

import com.fooddelivery.demo.Entities.Customer;
import com.fooddelivery.demo.Entities.DeliveryDriver;
import com.fooddelivery.demo.Entities.Restaurant;
import com.fooddelivery.demo.Entities.Review;
import com.fooddelivery.demo.Exceptions.ResourceNotFoundException;
import com.fooddelivery.demo.Repositories.CustomerRepository;
import com.fooddelivery.demo.Repositories.DeliveryDriverRepository;
import com.fooddelivery.demo.Repositories.RestaurantRepository;
import com.fooddelivery.demo.Repositories.ReviewRepository;
import com.fooddelivery.demo.dto.ResponseDTO.ReviewResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;

    public ReviewResponseDTO leaveRestaurantReview(Integer customerId, Integer restaurantId, int rating, String comment ) {
        Customer customer = customerRepository.findCustomerById(customerId) .orElseThrow(() -> ResourceNotFoundException.notFound( "Customer", customerId ) );
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId) .orElseThrow(() -> ResourceNotFoundException.notFound( "Restaurant", restaurantId ) );

        Review review = new Review();
        review.setCustomer(customer);
        review.setRestaurant(restaurant);
        review.setRating(rating);
        review.setComment(comment);
         review.setCreatedDate(LocalDateTime.now());
         review.setUpdatedDate(LocalDateTime.now());
         review.setIsActive(true);
         Review savedReview = reviewRepository.save(review);
         return ReviewResponseDTO.fromEntity( savedReview );
    }
 public ReviewResponseDTO leaveDriverReview( Integer customerId, Integer driverId, int rating, String comment ) {
        Customer customer = customerRepository.findCustomerById(customerId) .orElseThrow(() -> ResourceNotFoundException.notFound( "Customer", customerId ) );
        DeliveryDriver driver = deliveryDriverRepository.findDriverById(driverId) .orElseThrow(() -> ResourceNotFoundException.notFound( "Driver", driverId ) );

        Review review = new Review();
        review.setCustomer(customer);
        review.setDeliveryDriver(driver);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedDate( LocalDateTime.now() );
        review.setUpdatedDate( LocalDateTime.now() );
        review.setIsActive(true);

        return ReviewResponseDTO.fromEntity( reviewRepository.save(review) );
    }
    public List<ReviewResponseDTO> getDriverReviews(Integer driverId) {
        DeliveryDriver driver =deliveryDriverRepository.findDriverById(driverId).orElseThrow(() -> ResourceNotFoundException.notFound( "Driver", driverId ) );
        List<Review> reviews = reviewRepository.findReviewsByDriverId(driver.getId());
        return ReviewResponseDTO.fromEntity(reviews);
    }
    public String deleteReview(Integer reviewId) {
        Review review =reviewRepository.findReviewById(reviewId).orElseThrow(() -> ResourceNotFoundException.notFound( "Review", reviewId ) );
        review.setIsActive(false);
        review.setUpdatedDate(LocalDateTime.now());
        reviewRepository.save(review);
        return "DELETED";
    }
    public Double getRestaurantAverageRating( Integer restaurantId ) {
        Double averageRating = reviewRepository .getRestaurantAverageRating( restaurantId );
        return averageRating != null ? averageRating : 0.0;
    }
    public Double getDriverAverageRating( Integer driverId ) {
        Double averageRating = reviewRepository .getDriverAverageRating( driverId );
        return averageRating != null ? averageRating : 0.0;
    }
    public Page<ReviewResponseDTO> getRestaurantReviews(Integer restaurantId, int page, int size ) {
        Pageable pageable = PageRequest.of(page, size); Page<Review> reviews = reviewRepository .findRestaurantReviews( restaurantId, pageable );
        List<ReviewResponseDTO> response = new ArrayList<>();
        for (Review review : reviews.getContent()) {
            response.add( ReviewResponseDTO .fromEntity(review) );
    }
        return new PageImpl<>( response, pageable, reviews.getTotalElements() );
    }


}

