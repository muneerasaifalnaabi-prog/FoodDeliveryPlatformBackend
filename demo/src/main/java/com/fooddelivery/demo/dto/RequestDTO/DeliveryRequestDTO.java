package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.Delivery;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryRequestDTO {

    @NotNull(message = "Order id is required")
    private Integer orderId;

    @NotNull(message = "Driver id is required")
    private Integer driverId;

    @Pattern(regexp = "ASSIGNED|PICKED_UP|DELIVERED|CANCELLED", message = "Invalid delivery status")
    private String status;

    public Delivery toEntity() {

        Delivery delivery = new Delivery();
        delivery.setStatus(status != null ? status : "ASSIGNED");
        return delivery;
    }

    public void applyTo(Delivery delivery) {
        delivery.setStatus( status != null ? status : "ASSIGNED" );
    }
}