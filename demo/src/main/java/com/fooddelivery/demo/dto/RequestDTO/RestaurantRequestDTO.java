package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.Restaurant;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class RestaurantRequestDTO {
    @NotBlank(message = "Restaurant name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Cuisine type is required")
    private String cuisineType;

    @NotNull(message = "Opening time is required")
    private LocalTime openingTime;

    @NotNull(message = "Closing time is required")
    private LocalTime closingTime;

    @NotNull(message = "Minimum order amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum order amount must be greater than or equal to 0")
    private BigDecimal minOrderAmount;

    @NotNull(message = "Delivery fee is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Delivery fee must be greater than or equal to 0")
    private BigDecimal deliveryFee;

    private Boolean acceptingOrders;

    public Restaurant toEntity() {
        Restaurant restaurant = new Restaurant();
        applyTo(restaurant);

        return restaurant;
    }

    public void applyTo(Restaurant restaurant) {
        restaurant.setName(name);
        restaurant.setDescription(description);
        restaurant.setCuisineType(cuisineType);
        restaurant.setOpeningTime(openingTime);
        restaurant.setClosingTime(closingTime);
        restaurant.setMinOrderAmount(minOrderAmount);
        restaurant.setDeliveryFee(deliveryFee);
        restaurant.setAcceptingOrders(acceptingOrders != null ? acceptingOrders : true);
    }


}
