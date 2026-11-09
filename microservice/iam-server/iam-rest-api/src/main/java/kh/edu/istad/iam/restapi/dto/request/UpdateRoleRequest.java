package kh.edu.istad.iam.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateRoleRequest(
        @NotBlank
        @Size(min = 2, max = 50)
        String name,
        String description
) {}
