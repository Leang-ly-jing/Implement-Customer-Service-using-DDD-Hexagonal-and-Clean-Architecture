package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleJpaRepository extends JpaRepository<UserRoleEntity,Long> {

    List<UserRoleEntity> findByUserIdAndRevokedAtIsNull(Long userId);

    Optional<UserRoleEntity> findByUserIdAndRoleIdAndRevokedAtIsNull(Long userId, Long roleId);

}
