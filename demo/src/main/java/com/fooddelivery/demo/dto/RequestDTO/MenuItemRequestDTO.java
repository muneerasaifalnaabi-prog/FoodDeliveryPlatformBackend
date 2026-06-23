package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.MenuItem;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class MenuItemRequestDTO {

    @NotBlank(message = "Menu item name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    private BigDecimal price;

    private Boolean isAvailable;

    private Boolean isVegetarian;

    @NotNull(message = "Calories are required")
    @Min(value = 0, message = "Calories cannot be negative")
    private Integer calories;

    public MenuItem toEntity() {

        MenuItem menuItem = new MenuItem();
        applyTo(menuItem);

        return menuItem;
    }

    public void applyTo(MenuItem menuItem) {

        menuItem.setName(name);
        menuItem.setDescription(description);
        menuItem.setPrice(price);
        menuItem.setCalories(calories);
        menuItem.setIsAvailable(isAvailable != null ? isAvailable : true);

        menuItem.setIsVegetarian(isVegetarian != null ? isVegetarian : false);
    }
}