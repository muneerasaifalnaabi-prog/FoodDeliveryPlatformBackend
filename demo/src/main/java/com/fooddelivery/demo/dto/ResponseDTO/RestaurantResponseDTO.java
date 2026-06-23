package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.Restaurant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RestaurantResponseDTO {

    private Integer id;
    private String name;
    private String description;
    private String cuisineType;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private BigDecimal minOrderAmount;
    private BigDecimal deliveryFee;
    private Boolean acceptingOrders;
    private Integer ownerId;
    private String ownerName;

    public static RestaurantResponseDTO fromEntity(Restaurant entity) {

        RestaurantResponseDTO dto = new RestaurantResponseDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCuisineType(entity.getCuisineType());
        dto.setOpeningTime(entity.getOpeningTime());
        dto.setClosingTime(entity.getClosingTime());
        dto.setMinOrderAmount(entity.getMinOrderAmount());
        dto.setDeliveryFee(entity.getDeliveryFee());
        dto.setAcceptingOrders(entity.getAcceptingOrders());

        if (entity.getRestaurantOwner() != null) {
            dto.setOwnerId(entity.getRestaurantOwner().getId());
            dto.setOwnerName(entity.getRestaurantOwner().getFirstName() + " " + entity.getRestaurantOwner().getLastName());
        }

        return dto;
    }

    public static List<RestaurantResponseDTO> fromEntityList(List<Restaurant> entities) {

        List<RestaurantResponseDTO> dtos = new ArrayList<>();

        for (Restaurant entity : entities) {
            dtos.add(fromEntity(entity));
        }

        return dtos;
    }
}
