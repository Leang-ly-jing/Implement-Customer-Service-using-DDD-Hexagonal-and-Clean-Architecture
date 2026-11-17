package kh.edu.istad.iam.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "system_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfigEntity {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "password_min_length", nullable = false)
    private int passwordMinLength;

    @Column(name = "require_uppercase", nullable = false)
    private boolean requireUppercase;

    @Column(name = "require_digit", nullable = false)
    private boolean requireDigit;

    @Column(name = "require_special", nullable = false)
    private boolean requireSpecial;

    @Column(name = "max_failed_attempts", nullable = false)
    private int maxFailedAttempts;

    @Column(name = "lock_duration_minutes", nullable = false)
    private int lockDurationMinutes;

    @Column(name = "session_expiration_minutes", nullable = false)
    private int sessionExpirationMinutes;

    @Column(name = "access_token_ttl_minutes", nullable = false)
    private int accessTokenTtlMinutes;

    @Column(name = "refresh_token_ttl_days", nullable = false)
    private int refreshTokenTtlDays;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;
}
