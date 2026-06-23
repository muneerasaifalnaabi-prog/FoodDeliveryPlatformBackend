package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.Orders;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class OrdersRequestDTO {

    @NotNull(message = "Customer id is required")
    private Integer customerId;

    @NotNull(message = "Restaurant id is required")
    private Integer restaurantId;

    @Pattern(regexp = "PENDING|PREPARING|READY|DELIVERED|CANCELLED", message = "Invalid order status")
    private String status;

    @DecimalMin(value = "0.0", inclusive = true, message = "Discount amount must be valid")
    private BigDecimal discountAmount;

    private String deliveryNotes;

    @NotEmpty(message = "Order items are required")
    @Valid
    private List<OrderItemRequestDTO> orderItems;

    public Orders toEntity() {
        Orders order = new Orders();

        order.setStatus(status != null ? status : "PENDING");
        order.setDiscountAmount(discountAmount != null ? discountAmount : BigDecimal.ZERO);
        order.setDeliveryNotes(deliveryNotes);
        return order;
    }

    public void applyTo(Orders order) {

        order.setStatus(status);
        order.setDiscountAmount(discountAmount);
        order.setDeliveryNotes(deliveryNotes);
    }
}