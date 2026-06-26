package com.fooddelivery.demo.Controllers;

import com.fooddelivery.demo.Services.*;
import com.fooddelivery.demo.dto.ResponseDTO.CustomerResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.DeliveryDriverResponseDTO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
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
        return ResponseEntity.ok(reportingService.getRestaurantRevenueByDate(restaurantId,data));
    }
    @GetMapping("/orders/count/restaurant/{restaurantId}")
    public ResponseEntity<Integer> countRestaurantOrders(@PathVariable Integer restaurantId) {
        return ResponseEntity.ok(reportingService.countRestaurantOrders(restaurantId));

    }
    @GetMapping("/customers/top-loyalty")
    public ResponseEntity<List<CustomerResponseDTO>> getTop10LoyaltyCustomers() {
        return ResponseEntity.ok(reportingService.getTop10LoyaltyCustomers());
    }
    @GetMapping("/drivers/leaderboard")
    public ResponseEntity<List<DeliveryDriverResponseDTO>> getTop10DriverLeaderboard() {
        return ResponseEntity.ok(reportingService.getTop10DriverLeaderboard());
    }
    @GetMapping("/platform/daily-summary")
    public ResponseEntity<Map<String, Object>> getPlatformDailySummary(@RequestParam Date date ) {
        return ResponseEntity.ok( reportingService.getPlatformDailySummary(date) ); }

}
