package kh.edu.istad.iam.restapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateUserRequest (
        @NotBlank
        @Size(min = 3, max = 50)
        String userName,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 8, max = 100)
        String password,
        @NotBlank
        String confirmPassword,
        @NotBlank
        String givenName,
        @NotBlank
        String familyName,
        String gender,
        String phoneNumber,
        LocalDate birthDate,
        String zoneInfo,
        String locate,
        AddressRequest addressRequest,
        List<UUID> rolesUuids
){
    public record AddressRequest(
            String streetAddress,
            String locality,
            String region,
            String postalCode,
            String country
    ) {}
}
