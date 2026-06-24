package com.fooddelivery.demo.Services;

import com.fooddelivery.demo.Entities.MenuItem;
import com.fooddelivery.demo.Entities.Restaurant;
import com.fooddelivery.demo.Entities.RestaurantOwner;
import com.fooddelivery.demo.Exceptions.InvalidOrderStateException;
import com.fooddelivery.demo.Exceptions.ResourceNotFoundException;
import com.fooddelivery.demo.Repositories.MenuItemRepository;
import com.fooddelivery.demo.Repositories.RestaurantOwnerRepository;
import com.fooddelivery.demo.Repositories.RestaurantRepository;
import com.fooddelivery.demo.dto.RequestDTO.RestaurantRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.MenuItemResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.RestaurantResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RestaurantOwnerRepository restaurantOwnerRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;

    public RestaurantResponseDTO createRestaurant(RestaurantRequestDTO dto, Integer ownerId) {

        if (dto == null) {
            throw InvalidOrderStateException.invalidState("Restaurant data is required");
        }
        RestaurantOwner owner = restaurantOwnerRepository.findActiveById(ownerId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", ownerId));

        Restaurant restaurant = dto.toEntity();
        restaurant.setRestaurantOwner(owner);
        restaurant.setAcceptingOrders(true);
        restaurant.setCreatedDate(LocalDateTime.now());
        restaurant.setUpdatedDate(LocalDateTime.now());
        restaurant.setIsActive(true);

        return RestaurantResponseDTO.fromEntity(restaurantRepository.save(restaurant));
    }

    public RestaurantResponseDTO toggleAcceptingOrders(Integer restaurantId, boolean status) {

        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));

        restaurant.setAcceptingOrders(status);
        restaurant.setUpdatedDate(LocalDateTime.now());

        return RestaurantResponseDTO.fromEntity(restaurantRepository.save(restaurant));
    }

    public RestaurantResponseDTO updateDeliveryFee(Integer restaurantId, double newFee) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));

        restaurant.setDeliveryFee(BigDecimal.valueOf(newFee));
        restaurant.setUpdatedDate(LocalDateTime.now());

        return RestaurantResponseDTO.fromEntity(restaurantRepository.save(restaurant));

    }

    public List<RestaurantResponseDTO> getRestaurantsByCuisine(String cuisine) {
        List<Restaurant> restaurants = restaurantRepository.findByCuisineTypeIgnoreCase(cuisine);

        return RestaurantResponseDTO.fromEntity(restaurants);
    }

    public List<RestaurantResponseDTO> getRestaurantsUnderDeliveryFee(double maxFee) {
        List<Restaurant> restaurants = restaurantRepository.findByDeliveryFeeLessThanEqual(maxFee);

        return RestaurantResponseDTO.fromEntity(restaurants);
    }

    public List<MenuItemResponseDTO> getMenuForRestaurant(Integer restaurantId) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));
        List<MenuItem> menuItems = menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurant.getId());
        return MenuItemResponseDTO.fromEntity(menuItems);
    }

    public List<MenuItemResponseDTO> bulkUpdateMenuItemPrices(Integer restaurantId, double percentageIncrease) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));
        List<MenuItem> menuItems = menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurant.getId());
        for (MenuItem item : menuItems) {
            BigDecimal currentPrice = item.getPrice();
            BigDecimal increaseAmount = currentPrice.multiply(BigDecimal.valueOf(percentageIncrease / 100));
            BigDecimal updatedPrice = currentPrice.add(increaseAmount);
            item.setPrice(updatedPrice);
            item.setUpdatedDate(LocalDateTime.now());
            menuItemRepository.save(item);
        }
        return MenuItemResponseDTO.fromEntity(menuItems);

    }


}







