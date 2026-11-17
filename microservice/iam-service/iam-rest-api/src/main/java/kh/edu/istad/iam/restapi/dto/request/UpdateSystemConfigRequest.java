package kh.edu.istad.iam.restapi.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateSystemConfigRequest(
        @Min(4) @Max(128) int passwordMinLength,
        boolean requireUppercase,
        boolean requireDigit,
        boolean requireSpecial,
        @Min(1) @Max(100) int maxFailedAttempts,
        @Min(1) @Max(14400) int lockDurationMinutes,
        @Min(1) @Max(525600) int sessionExpirationMinutes,
        @Min(1) @Max(14400) int accessTokenTtlMinutes,
        @Min(1) @Max(365) int refreshTokenTtlDays
) {}
