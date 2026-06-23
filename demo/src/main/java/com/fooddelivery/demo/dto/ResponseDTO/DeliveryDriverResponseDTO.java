package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.DeliveryDriver;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DeliveryDriverResponseDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String driverCode;
    private String vehicleType;
    private String vehiclePlate;
    private Double currentLat;
    private Double currentLng;
    private Boolean isOnline;

    public static DeliveryDriverResponseDTO fromEntity(DeliveryDriver entity) {

        DeliveryDriverResponseDTO dto = new DeliveryDriverResponseDTO();

        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setDriverCode(entity.getDriverCode());
        dto.setVehicleType(entity.getVehicleType());
        dto.setVehiclePlate(entity.getVehiclePlate());
        dto.setCurrentLat(entity.getCurrentLat());
        dto.setCurrentLng(entity.getCurrentLng());
        dto.setIsOnline(entity.getIsOnline());

        return dto;
    }

    public static List<DeliveryDriverResponseDTO> fromEntity(List<DeliveryDriver> entities) {

        List<DeliveryDriverResponseDTO> dtos = new ArrayList<>();

        for (DeliveryDriver entity : entities) {
            dtos.add(fromEntity(entity));
        }

        return dtos;
    }
}
