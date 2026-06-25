package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerResponseDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer loyaltyPoints;
    private String customerCode;

    public static CustomerResponseDTO fromEntity(Customer entity) {

        CustomerResponseDTO dto = new CustomerResponseDTO();

        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setLoyaltyPoints(entity.getLoyaltyPoints());
        dto.setCustomerCode(entity.getCustomerCode());

        return dto;
    }
    public static List<CustomerResponseDTO> fromEntity(List<Customer> entities) {
        List<CustomerResponseDTO> dtos = new ArrayList<>();

        for (Customer entity : entities) {
            dtos.add(fromEntity(entity));
        }

        return dtos;
    }

}
