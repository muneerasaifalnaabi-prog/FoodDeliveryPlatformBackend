package com.fooddelivery.demo.dto.RequestDTO;

import com.fooddelivery.demo.Entities.CustomerAddress;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerAddressRequestDTO {
    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Building is required")
    private String building;

    private Boolean isDefault;

    public CustomerAddress convertToDTO() {

        CustomerAddress address = new CustomerAddress();

        address.setStreet(street);
        address.setCity(city);
        address.setBuilding(building);
        address.setIsDefault(isDefault != null ? isDefault : false);

        return address;
    }

}
