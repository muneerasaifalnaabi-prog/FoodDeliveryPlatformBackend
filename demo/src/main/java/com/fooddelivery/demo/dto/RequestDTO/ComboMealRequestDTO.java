package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.ComboMeal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class ComboMealRequestDTO {
    @NotBlank(message = "Combo meal name is required")
    private String comboName;

    private String description;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total price must be greater than or equal to 0")
    private BigDecimal totalPrice;

    private Boolean isAvailable;

    @NotEmpty(message = "Combo meal must contain at least one menu item")
    private List<Integer> menuItems;

    public ComboMeal toEntity() {
        ComboMeal comboMeal = new ComboMeal();
        applyTo(comboMeal);

        return comboMeal;
    }

    public void applyTo(ComboMeal comboMeal) {

        comboMeal.setComboName(comboName);
        comboMeal.setDescription(description);
        comboMeal.setTotalPrice(totalPrice);
        comboMeal.setIsAvailable(isAvailable != null ? isAvailable : comboMeal.getIsAvailable());
    }

}
