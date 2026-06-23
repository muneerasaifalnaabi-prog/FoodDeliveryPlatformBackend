package com.fooddelivery.demo.dto.SummaryDTO;

import com.fooddelivery.demo.Entities.DeliveryDriver;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryDriverSummaryDTO {
    private Integer id;
    private String firstName;
    private String lastName;

    public static DeliveryDriverSummaryDTO fromEntity(DeliveryDriver driver) {
        DeliveryDriverSummaryDTO dto = new DeliveryDriverSummaryDTO();
        dto.setId(driver.getId());
        dto.setFirstName(driver.getFirstName());
        dto.setLastName(driver.getLastName());
        return dto;
    }
}

