package com.fooddelivery.demo.dto.SummaryDTO;

import com.fooddelivery.demo.Entities.Restaurant;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RestaurantSummaryDTO {
    private Integer id;
    private String name;
    private String cuisineType;

    public static RestaurantSummaryDTO fromEntity(Restaurant restaurant) {
        RestaurantSummaryDTO dto = new RestaurantSummaryDTO();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setCuisineType(restaurant.getCuisineType());
        return dto;
    }
}
