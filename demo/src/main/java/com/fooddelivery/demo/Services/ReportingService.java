package com.fooddelivery.demo.Services;


import com.fooddelivery.demo.Entities.Restaurant;
import com.fooddelivery.demo.Exceptions.ResourceNotFoundException;
import com.fooddelivery.demo.Repositories.CustomerRepository;
import com.fooddelivery.demo.Repositories.DeliveryDriverRepository;
import com.fooddelivery.demo.Repositories.OrdersRepository;
import com.fooddelivery.demo.Repositories.RestaurantRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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


    public Double getRestaurantRevenue(Integer restaurantId, Data data) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId) .orElseThrow(() -> ResourceNotFoundException.notFound( "Restaurant", restaurantId ) );

        Double revenue = ordersRepository.calculateRestaurantRevenueByDate( restaurant.getId(), date );
        return revenue != null ? revenue :0.0;
    }



    }

}
