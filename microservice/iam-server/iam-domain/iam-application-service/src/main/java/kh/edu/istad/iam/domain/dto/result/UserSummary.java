package kh.edu.istad.iam.domain.dto.result;

import java.util.UUID;

public record UserSummary(
        UUID uuid,
        String username,
        String email,
        String name,
        boolean enabled
) {}
