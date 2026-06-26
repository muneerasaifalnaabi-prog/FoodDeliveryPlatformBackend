package com.fooddelivery.demo.Controllers;

import com.fooddelivery.demo.Services.*;
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
    @Autowired
    private ReportingService reportingService;

    @GetMapping("/revenue/restaurant/{restaurantId}")
    public ResponseEntity<Double> getRestaurantRevenue(@PathVariable Integer restaurantId, @RequestParam Data data) {
        return ResponseEntity.ok(reportingService.getRestaurantRevenue(restaurantId,data));
    }
    @GetMapping("/orders/count/restaurant/{restaurantId}")
    public ResponseEntity<Integer> totalOrders(@PathVariable Integer restaurantId) {
        return ResponseEntity.ok(reportingService.totalOrders(restaurantId));

    }
    @GetMapping("/customers/top-loyalty")
    public ResponseEntity<Integer> getTop10LoyaltyCustomers() {
        return ResponseEntity.ok(reportingService.getTop10LoyaltyCustomers());
    }
    @GetMapping("/drivers/leaderboard")
    public ResponseEntity<Integer> getTop10DriverLeaderboard() {
        return ResponseEntity.ok(reportingService.getTop10DriverLeaderboard());
    }
    @GetMapping("/platform/daily-summary")
    public ResponseEntity<Map<String, Object>> getPlatformDailySummary(@RequestParam Date date ) {
        return ResponseEntity.ok( reportingService.getPlatformDailySummary(date) ); }




}
