package com.fooddelivery.demo.Controllers;

import com.fooddelivery.demo.Services.ReportingService;
import com.fooddelivery.demo.dto.ResponseDTO.CustomerResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.DeliveryDriverResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportingController {

    @Autowired
    private ReportingService reportingService;

    @GetMapping("/revenue/restaurant/{restaurantId}")
    public ResponseEntity<Double> getRestaurantRevenueByDate(
            @PathVariable Integer restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportingService.getRestaurantRevenueByDate(restaurantId, date));
    }

    @GetMapping("/orders/count/restaurant/{restaurantId}")
    public ResponseEntity<Integer> countRestaurantOrders(@PathVariable Integer restaurantId) {
        return ResponseEntity.ok(reportingService.countRestaurantOrders(restaurantId));
    }

    @GetMapping("/customers/top-loyalty")
    public ResponseEntity<List<CustomerResponseDTO>> getTopLoyaltyCustomers() {
        return ResponseEntity.ok(reportingService.findTopLoyaltyCustomers());
    }

    @GetMapping("/drivers/leaderboard")
    public ResponseEntity<List<DeliveryDriverResponseDTO>> getDriverLeaderboard() {
        return ResponseEntity.ok(reportingService.getTop10DriverLeaderboard());
    }

    @GetMapping("/platform/daily-summary")
    public ResponseEntity<Map<String, Object>> getPlatformDailySummary(@RequestParam LocalDate date) {
        return ResponseEntity.ok(reportingService.getPlatformDailySummary(date));
    }

    @GetMapping("/revenue/restaurant/{restaurantId}/range")
    public ResponseEntity<Map<String, Object>> getRestaurantRevenueBetweenDates(@PathVariable Integer restaurantId, LocalDate from, @RequestParam LocalDate to) {
        return ResponseEntity.ok(reportingService.getRestaurantRevenue(restaurantId, from, to));
    }

    @GetMapping("/drivers/{driverId}/earnings")
    public ResponseEntity<Map<String, Object>> getDriverEarnings(@PathVariable Integer driverId, @RequestParam LocalDate from, @RequestParam LocalDate to) {
        return ResponseEntity.ok(reportingService.getDriverEarnings(driverId, from, to));
    }

    @GetMapping("/orders/cancellation-rate")
    public ResponseEntity<Map<String, Object>> getCancellationRate(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        return ResponseEntity.ok(reportingService.getCancellationRate(from, to));
    }

    @GetMapping("/platform/busiest-hours")
    public ResponseEntity<List<Object[]>> getBusiestHours() {
        return ResponseEntity.ok(reportingService.getBusiestHours());
    }
}