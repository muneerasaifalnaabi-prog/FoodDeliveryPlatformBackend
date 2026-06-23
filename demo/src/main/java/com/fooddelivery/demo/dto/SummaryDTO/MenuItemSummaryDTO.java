package com.fooddelivery.demo.dto.SummaryDTO;

import com.fooddelivery.demo.Entities.MenuItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MenuItemSummaryDTO {

    private Integer id;
    private String name;
    private BigDecimal price;
    private Boolean isVegetarian;

    public static MenuItemSummaryDTO fromEntity(MenuItem entity) {

        MenuItemSummaryDTO dto = new MenuItemSummaryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setIsVegetarian(entity.getIsVegetarian());

        return dto;
    }

    public static List<MenuItemSummaryDTO> fromEntityList(List<MenuItem> entities) {

        List<MenuItemSummaryDTO> dtos = new ArrayList<>();

        for (MenuItem entity : entities) {

            dtos.add(fromEntity(entity));

        }

        return dtos;
    }
}