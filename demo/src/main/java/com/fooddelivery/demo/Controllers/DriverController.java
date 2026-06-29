package com.fooddelivery.demo.Controllers;


import com.fooddelivery.demo.Services.DeliveryService;
import com.fooddelivery.demo.dto.RequestDTO.DeliveryDriverRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.DeliveryDriverResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.DeliveryResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {
    @Autowired
    private DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<DeliveryDriverResponseDTO> createDriver(@Valid @RequestBody DeliveryDriverRequestDTO driverRequestDTO){
        return new ResponseEntity<>(deliveryService.createDriver(driverRequestDTO), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<DeliveryDriverResponseDTO>> getAllDrivers() {
        return ResponseEntity.ok(deliveryService.getAllDrivers());
    }
    @GetMapping("/online")
    public ResponseEntity<List<DeliveryDriverResponseDTO>> getOnlineDrivers() {
        return ResponseEntity.ok(deliveryService.getOnlineDrivers());
    }
    @PutMapping("/{id}/status") 
    public ResponseEntity<String> toggleDriverOnlineStatus(@PathVariable Integer id, @RequestParam boolean isOnline ) {
        return ResponseEntity.ok( deliveryService.toggleDriverOnlineStatus(id, isOnline ));
    }
    @PutMapping("/{id}/location")
    public ResponseEntity<DeliveryDriverResponseDTO> updateDriverLocation( @PathVariable Integer id, @RequestParam double lat, @RequestParam double lng ) {
        return ResponseEntity.ok( deliveryService.updateDriverLocation(id,lat, lng));
    }
    @GetMapping("/{id}/deliveries")
    public ResponseEntity<List<DeliveryResponseDTO>> getDriverDeliveries( @PathVariable Integer id ) {
        return ResponseEntity.ok( deliveryService.getDriverDeliveries( id ) );
    }
    @GetMapping("/{id}/deliveries/active") public ResponseEntity<DeliveryResponseDTO> getActiveDriverDelivery( @PathVariable Integer id ) {
        return ResponseEntity.ok( deliveryService.getActiveDriverDelivery(id));
    }




}
