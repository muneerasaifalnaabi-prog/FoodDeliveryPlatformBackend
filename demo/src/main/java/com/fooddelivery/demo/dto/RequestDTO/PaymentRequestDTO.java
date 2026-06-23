package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.OrderItem;
import com.fooddelivery.demo.Entities.Payment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
    public class PaymentRequestDTO {
        @NotNull(message = "Order ID is required")
        private Integer orderId;

        @NotBlank(message = "Payment method is required")
        private String paymentMethod;

    public Payment toEntity() {

        Payment payment = new Payment();
        applyTo(payment);

        return payment;
    }

    public void applyTo(Payment payment) {

        payment.setPaymentMethod(paymentMethod);
    }
    }
}
