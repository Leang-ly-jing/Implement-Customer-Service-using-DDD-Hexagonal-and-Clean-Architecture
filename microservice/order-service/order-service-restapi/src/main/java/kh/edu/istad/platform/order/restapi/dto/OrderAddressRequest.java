package kh.edu.istad.platform.order.restapi.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderAddressRequest(
        @NotBlank(message = "Street is required")
        String street,
        @NotBlank(message = "Postal code is required")
        String postalCode,
        @NotBlank(message = "City is required")
        String city
) {
}
