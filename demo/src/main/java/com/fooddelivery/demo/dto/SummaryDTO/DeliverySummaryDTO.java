package com.fooddelivery.demo.dto.SummaryDTO;

import com.fooddelivery.demo.Entities.Delivery;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliverySummaryDTO {
    private Integer id;
    private String trackingCode;
    private String status;

    public static DeliverySummaryDTO fromEntity(Delivery delivery) {
        DeliverySummaryDTO dto = new DeliverySummaryDTO();
        dto.setId(delivery.getId());
        dto.setTrackingCode(delivery.getTrackingCode());
        dto.setStatus(delivery.getStatus());

        return dto;
    }

}
