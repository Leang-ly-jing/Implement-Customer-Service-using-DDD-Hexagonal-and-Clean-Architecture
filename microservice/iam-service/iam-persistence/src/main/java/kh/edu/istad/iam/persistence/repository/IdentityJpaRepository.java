package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.IdentityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IdentityJpaRepository extends JpaRepository<IdentityEntity, Long> {

    Optional<IdentityEntity> findByUuidAndIsDeletedFalse(UUID uuid);

    Optional<IdentityEntity> findByProviderAndProviderSubjectAndIsDeletedFalse(String provider, String providerSubject);
    List<IdentityEntity> findByUserIdAndIsDeletedFalse(Long userId);
    Optional<IdentityEntity> findByUserIdAndProviderAndIsDeletedFalse(Long userId, String provider);
}
