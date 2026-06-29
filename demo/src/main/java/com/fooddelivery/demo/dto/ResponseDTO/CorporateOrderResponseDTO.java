package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.CorporateOrder;
import com.fooddelivery.demo.dto.SummaryDTO.RestaurantSummaryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CorporateOrderResponseDTO {

    private Integer id;
    private String companyName;
    private String costCenter;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;
    private RestaurantSummaryDTO restaurant;
    private List<OrderItemResponseDTO> items;

    public static CorporateOrderResponseDTO fromEntity(CorporateOrder entity) {

        CorporateOrderResponseDTO dto = new CorporateOrderResponseDTO();

        dto.setId(entity.getId());
        dto.setCompanyName(entity.getCompanyName());
        dto.setCostCenter(entity.getCostCenter());

        // FIX: prevent null date crash
        dto.setOrderDate(entity.getOrderDate());

        dto.setStatus(entity.getStatus());
        dto.setTotalAmount(entity.getTotalAmount());

        if (entity.getRestaurant() != null) {
            dto.setRestaurant(RestaurantSummaryDTO.fromEntity(entity.getRestaurant()));
        }

        // FIX: null-safe items
        dto.setItems(
                entity.getCorporateOrderItems() != null
                        ? OrderItemResponseDTO.fromEntity(entity.getCorporateOrderItems())
                        : new ArrayList<>()
        );

        return dto;
    }

    public static List<CorporateOrderResponseDTO> fromEntity(List<CorporateOrder> entities) {

        List<CorporateOrderResponseDTO> dtos = new ArrayList<>();

        if (entities != null) {
            for (CorporateOrder entity : entities) {
                dtos.add(fromEntity(entity));
            }
        }

        return dtos;
    }
}