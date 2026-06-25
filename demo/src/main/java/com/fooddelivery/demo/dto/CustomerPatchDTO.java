package com.fooddelivery.demo.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerPatchDTO {
    @Size(min = 1,message = "First name must contain at least 2 characters")
    private  String firstName;

    @Size(min = 1,message = "First name must contain at least 2 characters")
    private  String lastName;

    @Pattern( regexp = "^\\+?[0-9]{8,15}$", message = "Phone number must be between 8 and 15 digits" )
    private String phoneNumber;
}
