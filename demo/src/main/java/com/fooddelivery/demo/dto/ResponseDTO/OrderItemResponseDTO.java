package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.OrderItem;
import com.fooddelivery.demo.dto.SummaryDTO.MenuItemSummaryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderItemResponseDTO {
    private Integer id;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String specialInstructions;
    private MenuItemSummaryDTO menuItem;

    public static OrderItemResponseDTO fromEntity(OrderItem entity) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();

        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setSpecialInstructions(entity.getSpecialInstructions());
        dto.setMenuItem(MenuItemSummaryDTO.fromEntity(entity.getMenuItem()));

        return dto;
    }

    public static List<OrderItemResponseDTO> fromEntity(List<OrderItem> entities) {

        List<OrderItemResponseDTO> dtos = new ArrayList<>();
        for (OrderItem entity : entities) {
            dtos.add(fromEntity(entity));
        }

        return dtos;
    }
}
