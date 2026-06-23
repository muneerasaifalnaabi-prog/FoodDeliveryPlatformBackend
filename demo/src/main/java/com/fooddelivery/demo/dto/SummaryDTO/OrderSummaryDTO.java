package com.fooddelivery.demo.dto.SummaryDTO;

import com.fooddelivery.demo.Entities.Orders;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderSummaryDTO {
    private Integer Id;
    private String orderCode;
    private String status;
    private BigDecimal totalAmount;

    public static OrderSummaryDTO fromEntity(Orders orders){
        OrderSummaryDTO dto = new OrderSummaryDTO();
        dto.setId(orders.getId());
        dto.setOrderCode(orders.getOrderCode());
        dto.setStatus(orders.getStatus());
        dto.setTotalAmount(orders.getTotalAmount());

        return dto;
    }
}
