package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.RestaurantOwner;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantOwnerRequestDTO {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Phone number must be between 8 and 15 digits")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must contain at least 6 characters")
    private String password;

    @NotBlank(message = "Business license code is required")
    private String businessLicenseCode;

    public RestaurantOwner toEntity() {
        RestaurantOwner owner = new RestaurantOwner();
        applyTo(owner);

        return owner;
    }

    public void applyTo(RestaurantOwner owner) {

        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setEmail(email);
        owner.setPhone(phone);

        owner.setPasswordHash(password);

        owner.setBusinessLicenseCode(businessLicenseCode);
    }
}
