package kh.edu.istad.platform.restapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RegisterBusinessRequest(
        @NotBlank(message = "Legal name is required")
        String legalName,
        @NotBlank(message = "Display name is required")
        String displayName,
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,
        @NotBlank(message = "Phone number is required")
        String phoneNumber,
        @NotEmpty(message = "Products list must not be empty")
        List<@Valid ProductRequest> products
) {}
