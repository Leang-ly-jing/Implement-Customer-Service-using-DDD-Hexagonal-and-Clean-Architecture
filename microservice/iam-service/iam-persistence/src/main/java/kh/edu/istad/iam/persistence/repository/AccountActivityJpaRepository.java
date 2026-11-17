package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.AccountActivityEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AccountActivityJpaRepository extends JpaRepository<AccountActivityEntity, Long> {

    List<AccountActivityEntity> findAllByUserUuidOrderByCreatedAtDesc(UUID userUuid, Pageable pageable);

    @Query("SELECT a FROM AccountActivityEntity a ORDER BY a.createdAt DESC")
    List<AccountActivityEntity> findRecentActivities(Pageable pageable);
}
