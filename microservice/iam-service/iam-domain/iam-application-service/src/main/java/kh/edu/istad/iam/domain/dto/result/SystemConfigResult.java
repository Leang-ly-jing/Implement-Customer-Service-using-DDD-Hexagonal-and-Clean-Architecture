package kh.edu.istad.iam.domain.dto.result;

import java.io.Serializable;

public record SystemConfigResult(
        int passwordMinLength,
        boolean requireUppercase,
        boolean requireDigit,
        boolean requireSpecial,
        int maxFailedAttempts,
        int lockDurationMinutes,
        int sessionExpirationMinutes,
        int accessTokenTtlMinutes,
        int refreshTokenTtlDays
) implements Serializable {

    public static SystemConfigResult defaultVal() {
        return new SystemConfigResult(8, true, true, true, 5, 15, 1440, 60, 30);
    }
}
