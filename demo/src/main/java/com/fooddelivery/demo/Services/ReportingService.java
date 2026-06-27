package com.fooddelivery.demo.Services;


import com.fooddelivery.demo.Entities.Customer;
import com.fooddelivery.demo.Entities.DeliveryDriver;
import com.fooddelivery.demo.Entities.Restaurant;
import com.fooddelivery.demo.Exceptions.ResourceNotFoundException;
import com.fooddelivery.demo.Repositories.CustomerRepository;
import com.fooddelivery.demo.Repositories.DeliveryDriverRepository;
import com.fooddelivery.demo.Repositories.OrdersRepository;
import com.fooddelivery.demo.Repositories.RestaurantRepository;
import com.fooddelivery.demo.dto.ResponseDTO.CustomerResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.DeliveryDriverResponseDTO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ReportingService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;


    public Double getRestaurantRevenueByDate(Integer restaurantId, Date date) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));

        Double revenue = ordersRepository.getRestaurantRevenueByDate(restaurant.getId(), date);
        return revenue != null ? revenue : 0.0;
    }

    public Integer countRestaurantOrders(Integer restaurantId) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));
        Integer totalOrder = ordersRepository.countRestaurantOrders(restaurant.getId());
        return totalOrder;
    }


    public List<CustomerResponseDTO> findTopLoyaltyCustomers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customers = customerRepository.findTopLoyaltyCustomers(pageable);
        List<CustomerResponseDTO> response = new ArrayList<>();
        for (Customer customer : customers) {
            response.add( CustomerResponseDTO .fromEntity(customer) );
        }
        return response;
    }


    public List<DeliveryDriverResponseDTO> getTop10DriverLeaderboard() {
        Pageable pageable = PageRequest.of(0, 10);
        List<DeliveryDriver> drivers = deliveryDriverRepository.getTop10DriverLeaderboard(pageable);

        List<DeliveryDriverResponseDTO> response = new ArrayList<>();
        for (DeliveryDriver driver : drivers) {
            response.add(DeliveryDriverResponseDTO.fromEntity(driver));
        }

        return response;
    }

    public Map<String, Object> getPlatformDailySummary(Date date) {
        Integer totalOrders = ordersRepository.countDailyOrders();
        BigDecimal deliveryFees = ordersRepository.getPlatformDailySummary();

        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        map.put("totalOrders", totalOrders);
        map.put("deliveryFees", deliveryFees);
        return map;

    }

}


