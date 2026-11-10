package kh.edu.istad.iam.persistence.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "given_name", nullable = false, length = 100)
    private String givenName;
    @Column(name = "family_name", nullable = false, length = 100)
    private String familyName;
    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "nickname", length = 100)
    private String nickname;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "profile_url", length = 500)
    private String profileUrl;

    @Column(name = "picture_url", length = 500)
    private String pictureUrl;

    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    @Column(name = "zoneinfo", length = 50)
    private String zoneinfo;

    @Column(name = "locale", nullable = false, length = 10)
    private String locale;

    @Column(name = "phone_number", unique = true, length = 20)
    private String phoneNumber;

    @Column(name = "phone_number_verified", nullable = false)
    private boolean phoneNumberVerified;

    @Column(name = "address_street_address")
    private String addressStreetAddress;

    @Column(name = "address_locality", length = 100)
    private String addressLocality;

    @Column(name = "address_region", length = 100)
    private String addressRegion;

    @Column(name = "address_postal_code", length = 20)
    private String addressPostalCode;

    @Column(name = "address_country", length = 2)
    private String addressCountry;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired;

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "password_changed_at")
    private Instant passwordChangedAt;
}