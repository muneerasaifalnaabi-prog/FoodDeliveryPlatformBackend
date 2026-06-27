package com.fooddelivery.demo.dto.RequestDTO;


import com.fooddelivery.demo.Entities.OrderItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class OrderItemRequestDTO {
    @NotNull(message = "Menu item id is required")
    private Integer menuItemId;

    @NotNull(message = "specify your quantity")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String specialInstructions;

    public OrderItem toEntity() {
        OrderItem orderItem = new OrderItem();
        applyTo(orderItem);

        return orderItem;
    }

    public void applyTo(OrderItem orderItem) {
        orderItem.setQuantity(quantity);
        orderItem.setSpecialInstructions(specialInstructions);

    }
}
