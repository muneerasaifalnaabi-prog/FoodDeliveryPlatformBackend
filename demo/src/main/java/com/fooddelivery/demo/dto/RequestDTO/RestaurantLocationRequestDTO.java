package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.RestaurantLocation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantLocationRequestDTO {
    private Double latitude;
    private Double longitude;

    public RestaurantLocation toEntity() {
        RestaurantLocation restaurantLocation = new RestaurantLocation();
        restaurantLocation.setLatitude(latitude);
        restaurantLocation.setLongitude(longitude);
        return restaurantLocation;

    }
}
