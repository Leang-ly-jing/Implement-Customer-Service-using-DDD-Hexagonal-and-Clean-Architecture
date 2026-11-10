package kh.edu.istad.iam.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;
public record GrantPermissionsToUserRequest(
        @NotEmpty
        List<UUID> permissionUuids,
        @NotBlank
        String effect,
        String reason
) {}