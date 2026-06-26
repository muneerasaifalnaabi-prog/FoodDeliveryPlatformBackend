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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


}

