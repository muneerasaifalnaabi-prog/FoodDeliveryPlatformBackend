package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.Delivery;
import com.fooddelivery.demo.dto.SummaryDTO.DeliveryDriverSummaryDTO;
import com.fooddelivery.demo.dto.SummaryDTO.OrderSummaryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DeliveryResponseDTO {

    private Integer id;
    private String trackingCode;
    private String status;
    private LocalDateTime assignedAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;
    private OrderSummaryDTO order;
    private DeliveryDriverSummaryDTO driver;

    public static DeliveryResponseDTO fromEntity(Delivery entity) {

        DeliveryResponseDTO dto = new DeliveryResponseDTO();

        dto.setId(entity.getId());
        dto.setTrackingCode(entity.getTrackingCode());
        dto.setStatus(entity.getStatus());
        dto.setAssignedAt(entity.getAssignedAt());
        dto.setPickedUpAt(entity.getPickedUpAt());
        dto.setDeliveredAt(entity.getDeliveredAt());
        dto.setOrder(OrderSummaryDTO.fromEntity(entity.getOrders()));
        dto.setDriver(DeliveryDriverSummaryDTO.fromEntity(entity.getDeliveryDriver()));

        return dto;
    }

    public static List<DeliveryResponseDTO> fromEntity(List<Delivery> entities) {

        List<DeliveryResponseDTO> dtos = new ArrayList<>();

        for (Delivery entity : entities) {
            dtos.add(fromEntity(entity));

        }

        return dtos;
    }
}