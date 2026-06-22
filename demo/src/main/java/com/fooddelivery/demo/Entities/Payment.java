package com.fooddelivery.demo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
    private String paymentMethod;
    private String status;
    private BigDecimal amount;
    private String transactionRef;
    private LocalDateTime processedAt;
    @OneToOne
    private Orders orders;

}
