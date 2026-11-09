package kh.edu.istad.iam.restapi.dto.response;

import java.time.Instant;
import java.util.UUID;
public record IdentityResponse(

        UUID uuid,
        String provider,
        String email,
        String displayName,
        Instant linkedAt
) {}