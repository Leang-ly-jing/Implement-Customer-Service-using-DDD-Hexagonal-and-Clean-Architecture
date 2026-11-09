package kh.edu.istad.iam.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfileEditForm(
        @NotBlank String givenName,
        @NotBlank String familyName,
        String locale
) {}

