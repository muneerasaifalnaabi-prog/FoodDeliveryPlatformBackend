package com.fooddelivery.demo.Controllers;

import com.fooddelivery.demo.Services.OrderService;
import com.fooddelivery.demo.Services.RestaurantService;
import com.fooddelivery.demo.dto.RequestDTO.ComboMealRequestDTO;
import com.fooddelivery.demo.dto.RequestDTO.MenuItemRequestDTO;
import com.fooddelivery.demo.dto.RequestDTO.RestaurantRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.ComboMealResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.MenuItemResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.RestaurantResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/owner/{ownerId}")
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(@PathVariable Integer ownerId, @Valid @RequestBody RestaurantRequestDTO dto) {
        return new ResponseEntity<>(restaurantService.createRestaurant(dto, ownerId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurant());
    }

    @GetMapping("{id}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(@PathVariable Integer id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @GetMapping("/cuisine/{cuisine}")
    public ResponseEntity<List<RestaurantResponseDTO>> getRestaurantByCuisine(@PathVariable String cuisine) {
        return ResponseEntity.ok(restaurantService.getRestaurantsByCuisine(cuisine));
    }

    @PutMapping("/{id}/toggle-orders")
    public ResponseEntity<RestaurantResponseDTO> toggleOrders(@PathVariable Integer id, @RequestParam Boolean accepting) {
        return ResponseEntity.ok(restaurantService.toggleAcceptingOrders(id, accepting));
    }


    @PutMapping("/{id}/fee/{newFee}")
    public ResponseEntity<RestaurantResponseDTO> updateDeliveryFee(@PathVariable Integer id, @PathVariable Double newFee) {
        return ResponseEntity.ok(restaurantService.updateDeliveryFee(id, newFee));
    }


    @GetMapping("/{id}/menu")
    public ResponseEntity<List<MenuItemResponseDTO>> getRestaurantMenu(@PathVariable Integer id) {
        return ResponseEntity.ok(restaurantService.getMenuForRestaurant(id));
    }

    @GetMapping("/{id}/combos")
    public ResponseEntity<List<ComboMealResponseDTO>> getRestaurantCombos(@PathVariable Integer id) {
        return ResponseEntity.ok(restaurantService.getRestaurantCombos(id));
    }

    @PostMapping("/{id}/menu")
    public ResponseEntity<MenuItemResponseDTO> addNewMenuItem(@PathVariable Integer id, @Valid @RequestBody MenuItemRequestDTO dto) {
        return new ResponseEntity<>(restaurantService.addNewMenuItem(id, dto), HttpStatus.CREATED);
    }

    @PutMapping("/menu/{itemId}/available")
    public ResponseEntity<MenuItemResponseDTO> updateMenuItemAvailability(@PathVariable Integer itemId, @RequestParam Boolean status) {
        return ResponseEntity.ok(restaurantService.updateMenuItemAvailability(itemId, status));
    }

    @PostMapping("/{id}/combos")
    public ResponseEntity<ComboMealResponseDTO> createNewComboMeal(@PathVariable Integer id, @Valid @RequestBody ComboMealRequestDTO dto) {
        return new ResponseEntity<>(restaurantService.createNewComboMeal(id, dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/bulk-price-increase")
    public ResponseEntity<List<MenuItemResponseDTO>> bulkUpdateMenuItemPrices(@PathVariable Integer id, @RequestParam Double percentage) {
        return ResponseEntity.ok(restaurantService.bulkUpdateMenuItemPrices(id, percentage));
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<Map<String, Object>> getRestaurantAnalytics(@PathVariable Integer id) {

        return ResponseEntity.ok(restaurantService.getRestaurantAnalytics(id));
    }

    @GetMapping("/near")
    public ResponseEntity<List<RestaurantResponseDTO>> getNearbyRestaurants(@RequestParam double lat, @RequestParam double lng, @RequestParam double radiusKm) {
        return ResponseEntity.ok(restaurantService.getNearbyRestaurants(lat, lng, radiusKm));
    }


    @GetMapping("/{id}/menu/top-sellers")
    public ResponseEntity<List<MenuItemResponseDTO>> getTopSellingMenuItems(@PathVariable Integer id) {
        return ResponseEntity.ok(restaurantService.getTopSellingMenuItems(id));
    }

    @GetMapping("/menu/search")
    public ResponseEntity<List<MenuItemResponseDTO>> searchMenuItems(@RequestParam String keyword, @RequestParam Integer minCalories, @RequestParam Integer maxCalories) {
        return ResponseEntity.ok(restaurantService.searchMenuItems(keyword, minCalories, maxCalories));
    }
}
