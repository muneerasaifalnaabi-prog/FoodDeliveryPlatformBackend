package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.RestaurantLocation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class RestaurantLocationResponseDTO {
    private Integer restaurantId;
    private Double latitude;
    private Double longitude;

    public static RestaurantLocationResponseDTO fromEntity(RestaurantLocation entity) {
        RestaurantLocationResponseDTO dto = new RestaurantLocationResponseDTO();
        dto.setRestaurantId(entity.getId());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        return dto;

    }
}
