package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.MenuItem;
import com.fooddelivery.demo.Entities.OrderItem;
import com.fooddelivery.demo.Entities.Orders;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderItemRequestDTO {
    @NotNull(message = "specify your quantity")
    private Integer quantity;

    private String specialInstructions;

    @NotEmpty(message = "Order must contain at least one menu item")
   // private List<Integer> menuItems;

    public OrderItem toEntity() {

        OrderItem orderItem = new OrderItem();
        applyTo(orderItem);

        return orderItem;
    }

    public void applyTo(OrderItem orderItem) {

        orderItem.setQuantity(quantity);
        orderItem.setSpecialInstructions(specialInstructions);
        //orderItem.setMenuItem(menuItems);
    }
}
