package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.CustomerAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerAddressResponseDTO {
    private Integer id;
    private String street;
    private String city;
    private String building;
    private Boolean isDefault;

    public static CustomerAddressResponseDTO convertToDTO(CustomerAddress entity) {

        CustomerAddressResponseDTO dto = new CustomerAddressResponseDTO();

        dto.setId(entity.getId());
        dto.setStreet(entity.getStreet());
        dto.setCity(entity.getCity());
        dto.setBuilding(entity.getBuilding());
        dto.setIsDefault(entity.getIsDefault());

        return dto;
    }

    public static List<CustomerAddressResponseDTO> convertToDTO(List<CustomerAddress> entities) {

        List<CustomerAddressResponseDTO> dtos = new ArrayList<>();

        for (CustomerAddress entity : entities) {
            dtos.add(convertToDTO(entity));
        }

        return dtos;
    }
}
