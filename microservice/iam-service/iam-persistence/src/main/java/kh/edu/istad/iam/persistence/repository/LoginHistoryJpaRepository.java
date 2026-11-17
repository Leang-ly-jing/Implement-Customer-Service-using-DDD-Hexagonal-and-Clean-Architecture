package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.LoginHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface LoginHistoryJpaRepository extends JpaRepository<LoginHistoryEntity, Long> {

    List<LoginHistoryEntity> findTop50ByUserUuidOrderByLoginAtDesc(UUID userUuid);

    Page<LoginHistoryEntity> findByUserUuidOrderByLoginAtDesc(UUID userUuid, Pageable pageable);

    Page<LoginHistoryEntity> findAllByOrderByLoginAtDesc(Pageable pageable);

    @Query("SELECT COUNT(DISTINCT l.username) FROM LoginHistoryEntity l " +
           "WHERE l.ipAddress = :ip AND l.status = :status AND l.loginAt >= :since")
    long countDistinctFailedUsernamesByIpSince(
            @Param("ip") String ip,
            @Param("status") String status,
            @Param("since") Instant since
    );

    @Query("SELECT COUNT(l) FROM LoginHistoryEntity l " +
           "WHERE LOWER(l.username) = LOWER(:username) AND l.status = :status AND l.loginAt >= :since")
    long countFailedAttemptsByUsernameSince(
            @Param("username") String username,
            @Param("status") String status,
            @Param("since") Instant since
    );

    long countByLoginAtGreaterThanEqual(Instant since);

    long countByStatusAndLoginAtGreaterThanEqual(String status, Instant since);

    long countByIsSuspiciousTrueAndLoginAtGreaterThanEqual(Instant since);

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM login_history WHERE id IN (" +
                   "  SELECT id FROM login_history WHERE login_at < :cutoff ORDER BY login_at ASC LIMIT :batchSize" +
                   ")", nativeQuery = true)
    int deleteOldBatch(@Param("cutoff") Instant cutoff, @Param("batchSize") int batchSize);
}
