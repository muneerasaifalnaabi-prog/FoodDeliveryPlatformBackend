package com.fooddelivery.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerPatchDTO {
        private String firstName;
        private String lastName;
        @Email
        private String email;
        @Pattern(regexp = "^\\+?[0-9]{8,15}$")
        private String phone;
        private String street;
        private String city;
        private String building;
    }

