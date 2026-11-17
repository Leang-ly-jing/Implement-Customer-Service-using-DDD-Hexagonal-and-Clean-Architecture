package kh.edu.istad.iam.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "login_history",
        indexes = {
                @Index(name = "idx_login_history_user_uuid", columnList = "user_uuid"),
                @Index(name = "idx_login_history_login_at", columnList = "login_at DESC"),
                @Index(name = "idx_login_history_ip_login_at", columnList = "ip_address, login_at DESC"),
                @Index(name = "idx_login_history_username_login_at", columnList = "username, login_at DESC"),
                @Index(name = "idx_login_history_suspicious_login_at", columnList = "is_suspicious, login_at DESC"),
                @Index(name = "idx_login_history_status_login_at", columnList = "status, login_at DESC")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid")
    private UUID userUuid;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "device")
    private String device;

    @Column(name = "device_type")
    private String deviceType;

    @Builder.Default
    @Column(name = "is_suspicious", nullable = false)
    private boolean isSuspicious = false;

    @Column(name = "suspicious_reason")
    private String suspiciousReason;

    @Column(name = "login_at", nullable = false)
    private Instant loginAt;
}
