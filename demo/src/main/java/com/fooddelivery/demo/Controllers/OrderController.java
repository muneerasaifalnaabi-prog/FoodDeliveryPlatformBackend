package com.fooddelivery.demo.Controllers;

import com.fooddelivery.demo.Services.OrderService;
import com.fooddelivery.demo.Services.RestaurantService;
import com.fooddelivery.demo.dto.RequestDTO.CorporateOrderRequestDTO;
import com.fooddelivery.demo.dto.RequestDTO.OrderItemRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.CorporateOrderResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.OrdersResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/customer/{customerId}/restaurant/{restaurantId}")
    public ResponseEntity<OrdersResponseDTO> createOrder(@PathVariable Integer customerId, @PathVariable Integer restaurantId, @Valid @RequestBody List<OrderItemRequestDTO> items) {
        return new ResponseEntity<>(orderService.createOrder(customerId, restaurantId, items), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<OrdersResponseDTO> createOrderItem(@PathVariable Integer id, @Valid @RequestBody OrderItemRequestDTO dto) {
        return ResponseEntity.ok(orderService.addMenuItemToOrder(id, dto.getMenuItemId(), dto.getQuantity()));
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<OrdersResponseDTO> deleteOrderItem(@PathVariable Integer id, @PathVariable Integer itemId) {
        orderService.removeMenuItemFromOrder(id, itemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/discount/{amount}")
    public ResponseEntity<OrdersResponseDTO> applyDiscount(@PathVariable Integer id, @PathVariable Double amount) {
        return ResponseEntity.ok(orderService.applyDiscount(id, amount));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<OrdersResponseDTO> confirmOrder(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, "PREPARING"));
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<OrdersResponseDTO> updateStatus(@PathVariable Integer id, @PathVariable String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdersResponseDTO> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrdersResponseDTO>> getRestaurantOrders(@PathVariable Integer restaurantId, @RequestParam String status) {
        return ResponseEntity.ok(orderService.getRestaurantOrders(restaurantId, status));
    }

    @PostMapping("/corporate")
    public ResponseEntity<CorporateOrderResponseDTO> createCorporateOrder(@Valid @RequestBody CorporateOrderRequestDTO dto) {
        return new ResponseEntity<>(orderService.placeCorporateOrder(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/timeline")
    public ResponseEntity<List<String>> getOrderTimeline(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderTimeline(id));
    }

    @PostMapping("/{id}/reorder")
    public ResponseEntity<OrdersResponseDTO> reorder(@PathVariable Integer id) {
        return new ResponseEntity<>(orderService.reorder(id), HttpStatus.CREATED);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<OrdersResponseDTO>> getCustomerOrdersFiltered(@PathVariable Integer customerId, @RequestParam(required = false) String status, @RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(orderService.getCustomerOrdersFiltered(customerId, status, from, to, page, size));
    }

    @GetMapping("/{id}/eta")
    public ResponseEntity<Map<String, Object>> getEstimatedDeliveryTime(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getEstimatedDeliveryTime(id)
        );
    }


}
