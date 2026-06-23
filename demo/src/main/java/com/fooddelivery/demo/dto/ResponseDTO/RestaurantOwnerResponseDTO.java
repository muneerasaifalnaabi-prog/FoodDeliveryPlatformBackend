package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.RestaurantOwner;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RestaurantOwnerResponseDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String businessLicenseCode;


    public static RestaurantOwnerResponseDTO fromEntity(RestaurantOwner entity) {

        RestaurantOwnerResponseDTO dto = new RestaurantOwnerResponseDTO();

        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setBusinessLicenseCode(entity.getBusinessLicenseCode());

        return dto;
    }

    public static List<RestaurantOwnerResponseDTO> fromEntity(List<RestaurantOwner> entities) {

        List<RestaurantOwnerResponseDTO> dtos = new ArrayList<>();

        for (RestaurantOwner entity : entities) {
            dtos.add(fromEntity(entity));
        }

        return dtos;
    }

}
