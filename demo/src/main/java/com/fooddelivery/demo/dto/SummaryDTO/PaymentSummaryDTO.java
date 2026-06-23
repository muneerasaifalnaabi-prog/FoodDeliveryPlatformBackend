package com.fooddelivery.demo.dto.SummaryDTO;

import com.fooddelivery.demo.Entities.Payment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PaymentSummaryDTO {
    private Integer Id;
    private String paymentMethod;
    private String status;
    private BigDecimal amount;

    public static PaymentSummaryDTO fromEntity(Payment payment) {
        PaymentSummaryDTO dto = new PaymentSummaryDTO();
        dto.setId(payment.getId());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setAmount(payment.getAmount());

        return dto;
    }
}
