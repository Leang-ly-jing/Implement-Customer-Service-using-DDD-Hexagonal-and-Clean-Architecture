package kh.edu.istad.iam.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
public record CreateRoleRequest(
        @NotBlank
        @Size(min = 2, max = 50)
        String name,
        String description,
        List<UUID> permissionUuids
) {}