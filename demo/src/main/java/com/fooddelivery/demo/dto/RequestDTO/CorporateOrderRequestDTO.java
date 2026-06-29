package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.CorporateOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorporateOrderRequestDTO {

    private String corporateCode;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Cost center is required")
    private String costCenter;

    @NotNull(message = "Restaurant id is required")
    private Integer restaurantId;

    @Pattern(regexp = "PENDING|PREPARING|READY|DELIVERED|CANCELLED", message = "Invalid order status")
    private String status;

    @DecimalMin(value = "0.0", inclusive = true, message = "Total amount must be valid")
    private BigDecimal totalAmount;

    @NotEmpty(message = "Corporate order items are required")
    @Valid
    private List<OrderItemRequestDTO> items;

    public CorporateOrder toEntity() {
        CorporateOrder order = new CorporateOrder();

        order.setCorporateCode(corporateCode);
        order.setCompanyName(companyName);
        order.setCostCenter(costCenter);
        order.setStatus(status != null ? status : "PENDING");
        order.setTotalAmount(totalAmount != null ? totalAmount : BigDecimal.ZERO);

        return order;
    }

    public void applyTo(CorporateOrder order) {
        order.setCorporateCode(corporateCode);
        order.setCompanyName(companyName);
        order.setCostCenter(costCenter);
        order.setStatus(status);
        order.setTotalAmount(totalAmount);
    }
}