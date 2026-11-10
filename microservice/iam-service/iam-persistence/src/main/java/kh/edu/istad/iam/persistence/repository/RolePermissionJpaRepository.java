package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolePermissionJpaRepository extends JpaRepository<RolePermissionEntity,Long> {

    List<RolePermissionEntity> findByRoleIdAndRevokedAtIsNull(Long roleId);
    Optional<RolePermissionEntity> findByRoleIdAndPermissionIdAndRevokedAtIsNull(Long roleId, Long permissionId);

}
