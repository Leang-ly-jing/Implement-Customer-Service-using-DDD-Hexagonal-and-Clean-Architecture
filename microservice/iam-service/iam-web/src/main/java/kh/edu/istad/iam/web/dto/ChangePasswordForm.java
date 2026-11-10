package kh.edu.istad.iam.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordForm(
        String currentPassword,
        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters long") String newPassword,
        @NotBlank String confirmPassword
) {}
