package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.ComboMeal;
import com.fooddelivery.demo.Entities.MenuItem;
import com.fooddelivery.demo.dto.SummaryDTO.MenuItemSummaryDTO;
import com.fooddelivery.demo.dto.SummaryDTO.RestaurantSummaryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ComboMealResponseDTO {

    private Integer id;
    private String comboName;
    private String description;
    private BigDecimal totalPrice;
    private Boolean isAvailable;
    private RestaurantSummaryDTO restaurant;
    private List<MenuItemSummaryDTO> menuItems;

    public static ComboMealResponseDTO fromEntity(ComboMeal entity) {

        ComboMealResponseDTO dto = new ComboMealResponseDTO();

        dto.setId(entity.getId());
        dto.setComboName(entity.getComboName());
        dto.setDescription(entity.getDescription());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setIsAvailable(entity.getIsAvailable());
        dto.setRestaurant(RestaurantSummaryDTO.fromEntity(entity.getRestaurant()));

        List<MenuItemSummaryDTO> items = new ArrayList<>();

        if (entity.getMenuItems() != null) {
            for (MenuItem menuItem : entity.getMenuItems()) {
                items.add(MenuItemSummaryDTO.fromEntity(menuItem));
            }
        }

        dto.setMenuItems(items);
        return dto;
    }

    public static List<ComboMealResponseDTO> fromEntityList(List<ComboMeal> entities) {

        List<ComboMealResponseDTO> dtos = new ArrayList<>();

        for (ComboMeal entity : entities) {

            dtos.add(fromEntity(entity));

        }

        return dtos;
    }
}