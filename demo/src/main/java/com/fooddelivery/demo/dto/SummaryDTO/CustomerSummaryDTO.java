package com.fooddelivery.demo.dto.SummaryDTO;

import com.fooddelivery.demo.Entities.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerSummaryDTO {
    private Integer id;
    private String firstName;
    private String lastName;

    public static CustomerSummaryDTO fromEntity(Customer customer) {
        CustomerSummaryDTO dto = new CustomerSummaryDTO();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        return dto;
    }

}
