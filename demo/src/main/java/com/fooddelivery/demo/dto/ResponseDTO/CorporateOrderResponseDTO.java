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
    private String corporateCode;
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
        dto.setCorporateCode(entity.getCorporateCode());
        dto.setCompanyName(entity.getCompanyName());
        dto.setCostCenter(entity.getCostCenter());
        dto.setOrderDate(entity.getOrderDate());
        dto.setStatus(entity.getStatus());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setRestaurant(RestaurantSummaryDTO.fromEntity(entity.getRestaurant()));
        dto.setItems(OrderItemResponseDTO.fromEntityList(entity.getCorporateOrderItems()));

        return dto;
    }

    public static List<CorporateOrderResponseDTO> fromEntityList(List<CorporateOrder> entities) {

        List<CorporateOrderResponseDTO> dtos = new ArrayList<>();

        for (CorporateOrder entity : entities) {
            dtos.add(fromEntity(entity));
        }
        return dtos;
    }
}