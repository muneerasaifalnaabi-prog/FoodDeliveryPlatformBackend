package com.fooddelivery.demo.dto;

import com.fooddelivery.demo.Entities.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static CustomerResponseDTO convertToDTO(Customer entity) {

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

}
