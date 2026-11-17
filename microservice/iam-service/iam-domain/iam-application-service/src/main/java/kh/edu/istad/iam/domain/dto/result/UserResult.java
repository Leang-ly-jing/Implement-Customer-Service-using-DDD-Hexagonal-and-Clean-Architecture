package kh.edu.istad.iam.domain.dto.result;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserResult(
        UUID uuid,
        String username,
        String email,
        boolean emailVerified,
        String name,
        String locale,
        boolean enabled,
        boolean accountNonLocked,
        List<RoleSummary> roles,
        Instant createdAt,
        Instant updatedAt
) {

}
