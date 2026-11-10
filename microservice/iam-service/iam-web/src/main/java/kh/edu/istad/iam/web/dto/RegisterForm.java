package kh.edu.istad.iam.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterForm(
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

        String locale
) {}

