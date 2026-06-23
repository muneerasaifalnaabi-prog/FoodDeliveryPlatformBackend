package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.Restaurant;
import com.fooddelivery.demo.dto.SummaryDTO.RestaurantOwnerSummaryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RestaurantResponseDTO {

    private Integer id;
    private String name;
    private String description;
    private String cuisineType;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private BigDecimal minOrderAmount;
    private BigDecimal deliveryFee;
    private Boolean acceptingOrders;
    private RestaurantOwnerSummaryDTO owner;

    public static RestaurantResponseDTO fromEntity(Restaurant restaurant) {
        RestaurantResponseDTO dto = new RestaurantResponseDTO();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setCuisineType(restaurant.getCuisineType());
        dto.setOpeningTime(restaurant.getOpeningTime());
        dto.setClosingTime(restaurant.getClosingTime());
        dto.setMinOrderAmount(restaurant.getMinOrderAmount());
        dto.setDeliveryFee(restaurant.getDeliveryFee());
        dto.setAcceptingOrders(restaurant.getAcceptingOrders());
        dto.setOwner(RestaurantOwnerSummaryDTO.fromEntity(restaurant.getRestaurantOwner()));
        return dto;
    }

    public static List<RestaurantResponseDTO> fromEntity(List<Restaurant> entities) {

        List<RestaurantResponseDTO> dtos = new ArrayList<>();

        for (Restaurant entity : entities) {
            dtos.add(fromEntity(entity));
        }

        return dtos;
    }
}
