package com.fooddelivery.demo.Services;

import com.fooddelivery.demo.Entities.ComboMeal;
import com.fooddelivery.demo.Entities.MenuItem;
import com.fooddelivery.demo.Entities.Restaurant;
import com.fooddelivery.demo.Entities.RestaurantOwner;
import com.fooddelivery.demo.Exceptions.DuplicateResourceException;
import com.fooddelivery.demo.Exceptions.InvalidOrderStateException;
import com.fooddelivery.demo.Exceptions.ResourceNotFoundException;
import com.fooddelivery.demo.Repositories.*;
import com.fooddelivery.demo.dto.RequestDTO.ComboMealRequestDTO;
import com.fooddelivery.demo.dto.RequestDTO.MenuItemRequestDTO;
import com.fooddelivery.demo.dto.RequestDTO.RestaurantRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.ComboMealResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.MenuItemResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.RestaurantResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RestaurantOwnerRepository restaurantOwnerRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private ComboMealRepository comboMealRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    //****========
    //create new Restaurant
    //==========****
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
    //****========
    //change the status of accepting order
    //==========****
    public RestaurantResponseDTO toggleAcceptingOrders( Integer restaurantId, Boolean status ) {
        Restaurant restaurant = restaurantRepository .findRestaurantById( restaurantId ) .orElseThrow(() -> ResourceNotFoundException.notFound( "Restaurant", restaurantId ) );
        restaurant.setAcceptingOrders( status );
        restaurant.setUpdatedDate( LocalDateTime.now() );
        return RestaurantResponseDTO .fromEntity( restaurantRepository.save( restaurant ) );
    }
    //****========
    //update delivery fee
    //==========****
    public RestaurantResponseDTO updateDeliveryFee(Integer restaurantId, double newFee) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));

        restaurant.setDeliveryFee(BigDecimal.valueOf(newFee));
        restaurant.setUpdatedDate(LocalDateTime.now());

        return RestaurantResponseDTO.fromEntity(restaurantRepository.save(restaurant));

    }
    //****========
    //find restaurant by cuisine type
    //==========****
    public List<RestaurantResponseDTO> getRestaurantsByCuisine(String cuisine) {
        List<Restaurant> restaurants = restaurantRepository.findByCuisineTypeIgnoreCase(cuisine);

        return RestaurantResponseDTO.fromEntity(restaurants);
    }
    //****========
    //find restaurant under delivery fee
    //==========****
    public List<RestaurantResponseDTO> getRestaurantsUnderDeliveryFee(double maxFee) {
        List<Restaurant> restaurants = restaurantRepository.findByDeliveryFeeLessThanEqual(maxFee);

        return RestaurantResponseDTO.fromEntity(restaurants);
    }
    //****========
    //get menu item for restaurant
    //==========****
    public List<MenuItemResponseDTO> getMenuForRestaurant(Integer restaurantId) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));
        List<MenuItem> menuItems = menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurant.getId());
        return MenuItemResponseDTO.fromEntity(menuItems);
    }
    //****========
    //Increase or update Item price
    //==========****
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
    //****========
    //get all restaurant
    //==========****
    public List<RestaurantResponseDTO> getAllRestaurant() {
        List<Restaurant> restaurants = restaurantRepository.getAllRestaurant();
        List<RestaurantResponseDTO> response = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            response.add(RestaurantResponseDTO.fromEntity(restaurant)
            );
        }

        return response;
    }
    //****========
    //Get restaurant by id
    //==========****
    public RestaurantResponseDTO getRestaurantById(Integer restaurantId) {
        if (restaurantRepository.findRestaurantById(restaurantId).isPresent()) {
            return RestaurantResponseDTO.fromEntity(restaurantRepository.findRestaurantById(restaurantId).get());
        }
        throw ResourceNotFoundException.notFound("Restaurant", restaurantId);
    }
    //****========
    //get current combo meals of that restaurant
    //==========****
    public List<ComboMealResponseDTO> getRestaurantCombos(Integer restaurantId) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));
        List<ComboMeal> comboMeals = comboMealRepository.findByRestaurantId(restaurantId);
        return ComboMealResponseDTO.fromEntity(comboMeals);
    }
    //****========
    //add new Menu item
    //==========****
    public MenuItemResponseDTO addNewMenuItem(Integer restaurantId, MenuItemRequestDTO dto) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));

        MenuItem item = dto.toEntity();
        item.setRestaurant(restaurant);
        item.setCreatedDate(LocalDateTime.now());
        item.setUpdatedDate(LocalDateTime.now());
        item.setIsActive(true);

        return MenuItemResponseDTO.fromEntity(menuItemRepository.save(item));
    }
    //****========
    //update Menu item available or not
    //==========****
    public MenuItemResponseDTO updateMenuItemAvailability(Integer itemId, boolean status) {
        MenuItem item = menuItemRepository.findMenuItemById(itemId).orElseThrow(() -> ResourceNotFoundException.notFound("Menu Item", itemId));

        item.setIsAvailable(status);
        item.setUpdatedDate(LocalDateTime.now());

        return MenuItemResponseDTO.fromEntity(menuItemRepository.save(item));
    }
    //****========
    //create new combo meals
    //==========****
    public ComboMealResponseDTO createNewComboMeal(Integer restaurantId, ComboMealRequestDTO dto) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));

        ComboMeal comboMeal = dto.toEntity();
        comboMeal.setRestaurant(restaurant);
        comboMeal.setCreatedDate(LocalDateTime.now());
        comboMeal.setUpdatedDate(LocalDateTime.now());
        comboMeal.setIsActive(true);
        List<MenuItem> menuItems = new ArrayList<>();

        for (Integer menuItemId : dto.getMenuItems()) {
            MenuItem menuItem = menuItemRepository.findMenuItemById(menuItemId).orElseThrow(() -> ResourceNotFoundException.notFound("Menu Item", menuItemId));

            if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
                throw DuplicateResourceException.alreadyExists(menuItem.getName(), "Menu item does not belong to this restaurant.");
            }
            menuItems.add(menuItem);
        }
        comboMeal.setMenuItems(menuItems);

        return ComboMealResponseDTO.fromEntity(comboMealRepository.save(comboMeal));
    }

    /* ==============check later=============
    public List<RestaurantResponseDTO> getNearbyRestaurants(double lat, double lng, double radiusKm ) {
        List<Restaurant> restaurants =restaurantRepository.findAllActiveRestaurants();
        List<RestaurantResponseDTO> restaurantResponseDTOS = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            Double distance= HelperUtils.calculateDistance(lat,lng,restaurant.getLatitude(), restaurant.getLongitude())
        }
    }

     */
    public Map<String, Object> getRestaurantAnalytics(Integer restaurantId) {
        restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));
        Double averageRating = reviewRepository.getRestaurantAverageRating(restaurantId);
        Double totalRevenue = ordersRepository.getRestaurantTotalRevenue(restaurantId);
        Integer completedOrders = ordersRepository.countRestaurantOrders(restaurantId);
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("averageRating", averageRating != null ? averageRating : 0.0);
        analytics.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        analytics.put("completedOrders", completedOrders);
        return analytics;
    }

    public List<MenuItemResponseDTO> getTopSellingMenuItems(Integer restaurantId) {
        List<MenuItem> menuItems = menuItemRepository.findTopSellingMenuItems(restaurantId);
        List<MenuItemResponseDTO> response = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            response.add(MenuItemResponseDTO.fromEntity(menuItem));
        }
        return response;
    }

    public List<MenuItemResponseDTO> searchMenuItems(String keyword, Integer minCalories, Integer maxCalories) {
        List<MenuItem> menuItems = menuItemRepository.searchMenuItems(keyword, minCalories, maxCalories);
        List<MenuItemResponseDTO> response = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            response.add(MenuItemResponseDTO.fromEntity(menuItem));
        }
        return response;
    }

    public Double getRestaurantRevenueBetweenDates(Integer restaurantId, LocalDateTime from, LocalDateTime to) {
        Double revenue = ordersRepository.getRestaurantRevenueBetweenDates(restaurantId, from, to);
        return revenue != null ? revenue : 0.0;
    }
    public List<RestaurantResponseDTO> searchRestaurants(String keyword, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants = restaurantRepository.searchRestaurantsByKeyword(keyword, pageable);
        List<RestaurantResponseDTO> response = new ArrayList<>();

        for (Restaurant restaurant : restaurants.getContent()) {
            response.add(RestaurantResponseDTO.fromEntity(restaurant));
        }
        return response;
    }
    /*
    public List<RestaurantResponseDTO> getNearbyRestaurants( double lat, double lng, double radiusKm ) {
        List<Restaurant> restaurants = restaurantRepository.getAllRestaurant();
        List<RestaurantResponseDTO> response = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            double distance = HelperUtils.calculateDistance( lat, lng, restaurant.getLatitude(), restaurant.getLongitude() );
            if (distance <= radiusKm) {
                response.add( RestaurantResponseDTO.fromEntity( restaurant ) );
            }
        }
        return response;
    }

     */




}







