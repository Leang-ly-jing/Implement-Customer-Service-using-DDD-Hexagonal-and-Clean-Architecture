package kh.edu.istad.iam.restapi.dto.response;

import lombok.Builder;
import java.util.Set;
@Builder
public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        String username,
        Set<String> roles
) {}