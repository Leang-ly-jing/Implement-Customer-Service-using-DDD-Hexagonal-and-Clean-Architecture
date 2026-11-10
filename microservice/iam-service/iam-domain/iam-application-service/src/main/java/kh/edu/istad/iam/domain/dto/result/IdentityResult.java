package kh.edu.istad.iam.domain.dto.result;

import java.time.Instant;
import java.util.UUID;

public record IdentityResult(
        UUID uuid,
        String provider,
        String email,
        String displayName,
        Instant linkedAt
) {}
