package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionJpaRepository extends JpaRepository<UserSessionEntity, UUID> {

    List<UserSessionEntity> findAllByUserUuidAndRevokedAtIsNullOrderByLastActiveAtDesc(UUID userUuid);

    List<UserSessionEntity> findAllByUserUuidOrderByLastActiveAtDesc(UUID userUuid);

    Optional<UserSessionEntity> findByIdAndUserUuidAndRevokedAtIsNull(UUID id, UUID userUuid);

    Optional<UserSessionEntity> findByIdAndRevokedAtIsNull(UUID id);

    boolean existsByIdAndRevokedAtIsNull(UUID id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserSessionEntity s SET s.revokedAt = :revokedAt WHERE s.id = :id AND s.userUuid = :userUuid AND s.revokedAt IS NULL")
    void revokeSession(@Param("id") UUID id, @Param("userUuid") UUID userUuid, @Param("revokedAt") Instant revokedAt);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserSessionEntity s SET s.revokedAt = :revokedAt WHERE s.userUuid = :userUuid AND s.id <> :currentSid AND s.revokedAt IS NULL")
    void revokeOtherSessions(@Param("userUuid") UUID userUuid, @Param("currentSid") UUID currentSid, @Param("revokedAt") Instant revokedAt);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserSessionEntity s SET s.revokedAt = :revokedAt WHERE s.userUuid = :userUuid AND s.revokedAt IS NULL")
    void revokeAllSessions(@Param("userUuid") UUID userUuid, @Param("revokedAt") Instant revokedAt);
}