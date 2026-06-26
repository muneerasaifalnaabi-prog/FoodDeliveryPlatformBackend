package com.fooddelivery.demo.Controllers;

import com.fooddelivery.demo.Services.CustomerService;
import com.fooddelivery.demo.Services.DeliveryService;
import com.fooddelivery.demo.Services.OrderService;
import com.fooddelivery.demo.Services.RestaurantService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportingController {
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private OrderService orderService;
    @Autowired
    DeliveryService deliveryService;
    @Autowired
    CustomerService customerService;

    @GetMapping("/revenue/restaurant/{restaurantId}")
    public ResponseEntity<BigDecimal> getRestaurantRevenue(@PathVariable Integer restaurantId, @RequestParam Data data) {
        return ResponseEntity.ok(restaurantService.getRestaurantRevenue(restaurantId,data));
    }
    @GetMapping("/orders/count/restaurant/{restaurantId}")
    public ResponseEntity<Long> totalOrders(@PathVariable Integer restaurantId) {
        return ResponseEntity.ok(restaurantService.totalOrders(restaurantId));

    }
    @GetMapping("/customers/top-loyalty")
    public ResponseEntity<Integer> getTop10LoyaltyCustomers() {
        return ResponseEntity.ok(customerService.getTop10LoyaltyCustomers());
    }
    @GetMapping("/drivers/leaderboard")
    public ResponseEntity<Integer> getTop10DriverLeaderboard() {
        return ResponseEntity.ok(deliveryService.getTop10DriverLeaderboard());
    }
    @GetMapping("/platform/daily-summary")
    public ResponseEntity<Map<String, Object>> getPlatformDailySummary(@RequestParam Date date ) {
        return ResponseEntity.ok( orderService.getPlatformDailySummary(date) ); }




}
