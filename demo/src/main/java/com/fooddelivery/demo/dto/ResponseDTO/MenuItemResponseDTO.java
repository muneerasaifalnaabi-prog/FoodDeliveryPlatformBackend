package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.MenuItem;
import com.fooddelivery.demo.dto.SummaryDTO.RestaurantSummaryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class MenuItemResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean isAvailable;
    private Boolean isVegetarian;
    private Integer calories;
    private RestaurantSummaryDTO restaurant;

    public static MenuItemResponseDTO fromEntity(MenuItem menuItem) {
        MenuItemResponseDTO dto = new MenuItemResponseDTO();
        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setIsAvailable(menuItem.getIsAvailable());
        dto.setIsVegetarian(menuItem.getIsVegetarian());
        dto.setCalories(menuItem.getCalories());
        dto.setRestaurant( RestaurantSummaryDTO.fromEntity( menuItem.getRestaurant() ) );
        return dto;
    }
}
