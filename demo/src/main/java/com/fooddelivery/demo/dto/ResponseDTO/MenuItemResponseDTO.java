package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.MenuItem;
import com.fooddelivery.demo.dto.SummaryDTO.RestaurantSummaryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    public static List<MenuItemResponseDTO> fromEntity(List<MenuItem> menuItems) {
        List<MenuItemResponseDTO> dtos = new ArrayList<>();
        if (menuItems != null) {
            for (MenuItem menuItem : menuItems) {
                dtos.add(fromEntity(menuItem));
            }
        }
        return dtos;
    }
}
