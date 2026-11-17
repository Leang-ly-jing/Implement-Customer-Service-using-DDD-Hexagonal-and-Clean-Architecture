package kh.edu.istad.iam.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "account_activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "metadata")
    private String metadata;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "device")
    private String device;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
