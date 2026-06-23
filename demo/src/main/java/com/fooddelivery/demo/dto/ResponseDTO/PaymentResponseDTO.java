package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.Payment;
import com.fooddelivery.demo.dto.SummaryDTO.OrderSummaryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PaymentResponseDTO {

    private Integer id;
    private String paymentMethod;
    private String status;
    private BigDecimal amount;
    private String transactionRef;
    private LocalDateTime processedAt;
    private OrderSummaryDTO order;

    public static PaymentResponseDTO fromEntity(Payment entity) {
        PaymentResponseDTO dto = new PaymentResponseDTO();

        dto.setId(entity.getId());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setStatus(entity.getStatus());
        dto.setAmount(entity.getAmount());
        dto.setTransactionRef(entity.getTransactionRef());
        dto.setProcessedAt(entity.getProcessedAt());
        dto.setOrder(OrderSummaryDTO.fromEntity(entity.getOrders()));

        return dto;
    }

    public static List<PaymentResponseDTO> fromEntityList(List<Payment> entities) {
        List<PaymentResponseDTO> dtos = new ArrayList<>();

        for (Payment entity : entities) {
            dtos.add(fromEntity(entity));
        }
        return dtos;
    }
}