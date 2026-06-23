package com.fooddelivery.demo.dto.SummaryDTO;

import com.fooddelivery.demo.Entities.RestaurantOwner;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantOwnerSummaryDTO {
    private Integer id;
    private String firstName;
    private String lastName;

    public static RestaurantOwnerSummaryDTO fromEntity(RestaurantOwner owner) {
        RestaurantOwnerSummaryDTO dto = new RestaurantOwnerSummaryDTO();
        dto.setId(owner.getId());
        dto.setFirstName(owner.getFirstName());
        dto.setLastName(owner.getLastName());
        return dto;
    }

}
