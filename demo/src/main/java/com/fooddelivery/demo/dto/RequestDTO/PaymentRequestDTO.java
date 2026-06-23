package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.Payment;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PaymentRequestDTO {

    @NotNull(message = "Order id is required")
    private Integer orderId;

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "CASH|CARD|ONLINE", message = "Invalid payment method")
    private String paymentMethod;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Amount must be greater than or equal to 0")
    private BigDecimal amount;

    public Payment toEntity() {
        Payment payment = new Payment();

        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(amount);
        payment.setStatus("PENDING");
        return payment;
    }

    public void applyTo(Payment payment) {

        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(amount);
    }
}