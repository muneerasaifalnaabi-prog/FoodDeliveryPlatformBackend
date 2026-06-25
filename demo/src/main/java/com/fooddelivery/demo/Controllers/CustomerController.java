package com.fooddelivery.demo.Controllers;

import com.fooddelivery.demo.Services.CustomerService;
import com.fooddelivery.demo.dto.RequestDTO.CustomerAddressRequestDTO;
import com.fooddelivery.demo.dto.RequestDTO.CustomerRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.CustomerAddressResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.CustomerResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.OrdersResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO dto) {
        return new ResponseEntity<>(customerService.createCustomer(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerResponseDTO> getCustomerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateCustomerById(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.deactivateCustomer(id));
    }

    @PutMapping("/{id}/loyalty/add/{points}")
    public ResponseEntity<CustomerResponseDTO> addLoyaltyPoints(@PathVariable Integer id, @PathVariable Integer points) {
        return ResponseEntity.ok(customerService.updateLoyaltyPoints(id, points));
    }

    @PutMapping("/{id}/loyalty/add/{points}")
    public ResponseEntity<CustomerResponseDTO> deductLoyaltyPoints(@PathVariable Integer id, @PathVariable Integer points) {
        return ResponseEntity.ok(customerService.updateLoyaltyPoints(id, points));
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<CustomerAddressResponseDTO> addAddress(@PathVariable Integer id, @Valid @RequestBody CustomerAddressRequestDTO addressDTO) {
        return new ResponseEntity<>(customerService.addAddress(id, addressDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<CustomerAddressResponseDTO>> getCustomerAddresses(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.getCustomerAddresses(id));
    }

    @PutMapping("/addresses/{addressId}/default")
    public ResponseEntity<CustomerAddressResponseDTO> setDefaultAddress(@PathVariable Integer addressId) {
        return ResponseEntity.ok(customerService.setDefaultAddress(addressId));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Integer addressId) {
        return new ResponseEntity<>(customerService.deleteAddress(addressId), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrdersResponseDTO>> getCustomerOrders(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.getCustomerOrders(id));
    }

}
