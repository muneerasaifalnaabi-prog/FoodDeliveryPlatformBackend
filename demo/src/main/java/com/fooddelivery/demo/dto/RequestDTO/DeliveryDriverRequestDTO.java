package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.DeliveryDriver;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryDriverRequestDTO {
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

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    @NotBlank(message = "Vehicle plate is required")
    private String vehiclePlate;

    @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90")
    @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90")
    private Double currentLat;

    @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180")
    @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180")
    private Double currentLng;

    private Boolean isOnline;

    public DeliveryDriverRequestDTO convertToDTO(DeliveryDriver entity) {

        DeliveryDriverRequestDTO driver = new DeliveryDriverRequestDTO();

        driver.setFirstName(firstName);
        driver.setLastName(lastName);
        driver.setEmail(email);
        driver.setPhone(phone);
        driver.setPassword(password);
        driver.setVehicleType(vehicleType);
        driver.setVehiclePlate(vehiclePlate);
        driver.setCurrentLat(currentLat);
        driver.setCurrentLng(currentLng);

        driver.setIsOnline(isOnline != null ? isOnline : false);

        return driver;

    }
}
