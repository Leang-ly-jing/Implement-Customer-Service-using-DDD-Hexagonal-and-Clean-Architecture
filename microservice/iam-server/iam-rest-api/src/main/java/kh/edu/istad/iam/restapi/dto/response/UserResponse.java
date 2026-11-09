package kh.edu.istad.iam.restapi.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID uuid,
        String username,
        String email,
        boolean emailVerified,
        String name,
        String locale,
        boolean enabled,
        List<RoleSummaryResponse> roles,
        Instant createdAt,
        Instant updatedAt
) {
    public record RoleSummaryResponse(
            UUID uuid,
            String name) {}
}