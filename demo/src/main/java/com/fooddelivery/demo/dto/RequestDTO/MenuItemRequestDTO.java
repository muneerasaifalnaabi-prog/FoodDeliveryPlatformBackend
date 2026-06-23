package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.MenuItem;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuItemRequestDTO {
    @NotBlank(message = "Item name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private Double price;

    @NotNull(message = "Vegetarian flag must be specified")
    private Boolean isVegetarian;

    @PositiveOrZero(message = "Calories cannot be negative")
    private Integer calories;

    public MenuItemRequestDTO convertToDTO(MenuItem entity) {
        MenuItemRequestDTO item = new MenuItemRequestDTO();
        item.setName(entity.getName());
        item.setDescription(entity.getDescription());
        item.setPrice(entity);
        item.setIsVegetarian(this.isVegetarian);
        item.setCalories(this.calories);
        item.setIsAvailable(true);
        return item;

}
