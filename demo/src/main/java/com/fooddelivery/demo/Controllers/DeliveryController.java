package com.fooddelivery.demo.Controllers;

import com.fooddelivery.demo.Services.DeliveryService;
import com.fooddelivery.demo.dto.ResponseDTO.DeliveryDriverResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.DeliveryResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {
    @Autowired
    DeliveryService  deliveryService;

    @PostMapping("/order/{orderId}/assign-manual/{driverId}")
    public ResponseEntity<DeliveryResponseDTO> assignDriverToOrders(@PathVariable Integer orderId, @PathVariable Integer driverId){
        return ResponseEntity.ok(deliveryService.assignDriverToOrders(orderId, driverId));
    }
    @PostMapping("/order/{orderId}/assign-auto")
    public  ResponseEntity<DeliveryResponseDTO> autoAssignDriver(@PathVariable Integer orderId){
        return ResponseEntity.ok(deliveryService.autoAssignDriver(orderId));
    }
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponseDTO> getDeliveryById( @PathVariable Integer id ) {
        return ResponseEntity.ok( deliveryService.getDeliveryById(id));
    }
    @PutMapping("/{id}/pickup")
    public ResponseEntity<DeliveryResponseDTO> pickupDelivery(@PathVariable Integer id) {
        return ResponseEntity.ok(deliveryService.markDeliveryPickedUp(id));
    }
    @PutMapping("/{id}/complete")
    public ResponseEntity<DeliveryResponseDTO> markDeliveryDelivered( @PathVariable Integer id ) {
        return ResponseEntity.ok( deliveryService.markDeliveryDelivered( id ) );
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeliveryResponseDTO>> getDeliveriesByStatus(@PathVariable String status ) {
        return ResponseEntity.ok( deliveryService.getDeliveriesByStatus(status));
    }
    @GetMapping("/drivers/nearby") public ResponseEntity<List<DeliveryDriverResponseDTO>> getNearbyDrivers(@RequestParam double lat, @RequestParam double lng, @RequestParam double radiusKm ) {
        return ResponseEntity.ok( deliveryService.getNearbyDrivers( lat, lng, radiusKm ) );
    }
    @GetMapping("/drivers/{driverId}/performance") public ResponseEntity<Map<String, Object>> getDriverPerformance(@PathVariable Integer driverId ) {
        return ResponseEntity.ok( deliveryService.getDriverPerformance( driverId ) );
    }





}
