package kh.edu.istad.iam.domain.dto.result;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record UserAuthoritiesResult(
        List<AssignedRoleSummary> roles,
        List<DirectPermissionSummary> directPermissions,
        Set<String> effectivePermissions
) {
    public record AssignedRoleSummary(
            UUID uuid,
            String name,
            Instant expiresAt
    ) {}
    public record DirectPermissionSummary(
            String name,
            String effect
    ) {}
}