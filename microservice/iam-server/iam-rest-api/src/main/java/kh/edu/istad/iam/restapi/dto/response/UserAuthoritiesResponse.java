package kh.edu.istad.iam.restapi.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserAuthoritiesResponse(
        List<RoleAuthority> roles,
        List<DirectPermission> directPermissions,
        List<String> effectivePermissions
) {
    public record RoleAuthority(
            UUID uuid,
            String name,
            Instant expiresAt)
    {}

    public record DirectPermission(
            String name,
            String effect)
    {}
}