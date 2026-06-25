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

        return ReviewResponseDTO.fromEntity( reviewRepository.save(review) ); } }

