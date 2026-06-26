package com.fooddelivery.demo.Controllers;

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

@RestController
@RequestMapping
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

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
    public ResponseEntity<List<RestaurantResponseDTO>> toggleOrders(@PathVariable Integer id, @RequestParam Boolean accepting) {
        return ResponseEntity.ok((List<RestaurantResponseDTO>) restaurantService.toggleAcceptingOrders(id, accepting));

    }

    @PutMapping("/{id}/fee/{newFee}")
    public ResponseEntity<RestaurantResponseDTO> updateDeliveryFee(@PathVariable Integer id, @PathVariable Double newFee) {
        return ResponseEntity.ok(restaurantService.updateDeliveryFee(id, newFee));
    }

    //....check  this <added list in getMenuForRestaurant !!
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
        return new ResponseEntity<>(restaurantService.addNewMenuItem(id,dto), HttpStatus.CREATED);
    }

    @PutMapping("/menu/{itemId}/")
    public ResponseEntity<MenuItemResponseDTO> updateMenuItemAvailability(@PathVariable Integer itemId, @RequestParam Boolean status) {
        return new ResponseEntity<>(restaurantService.updateMenuItemAvailability(itemId,status), HttpStatus.NOT_FOUND);
    }

    /*@PostMapping("/{id}/combos")
    public ResponseEntity<ComboMealResponseDTO> createNewComboMeal(@PathVariable Integer id, @Valid @RequestBody ComboMealRequestDTO dto) {
        return new ResponseEntity<>(restaurantService.createNewComboMeal(id,dto), HttpStatus.CREATED);
    }

     */

    @PutMapping("/{id}/bulk-price-increase")
    public ResponseEntity<List<MenuItemResponseDTO>> bulkUpdateMenuItemPrices(@PathVariable Integer id, @RequestParam Double percentage) {
        return ResponseEntity.ok(restaurantService.bulkUpdateMenuItemPrices(id, percentage));
    }

}
