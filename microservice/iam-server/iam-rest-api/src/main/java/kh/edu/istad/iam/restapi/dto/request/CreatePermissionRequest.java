package kh.edu.istad.iam.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record CreatePermissionRequest(
        @NotBlank
        @Size(max = 50)
        String resource,
        @NotBlank @Size(max = 50)
        String action,
        String description
) {}