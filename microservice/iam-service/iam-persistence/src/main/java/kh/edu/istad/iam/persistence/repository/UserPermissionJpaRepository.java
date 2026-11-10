package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.UserPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPermissionJpaRepository extends JpaRepository<UserPermissionEntity,Long> {

    List<UserPermissionEntity> findByUserIdAndRevokedAtIsNull(Long userId);
    Optional<UserPermissionEntity> findByUserIdAndPermissionIdAndRevokedAtIsNull(Long userId, Long permissionId);

}
