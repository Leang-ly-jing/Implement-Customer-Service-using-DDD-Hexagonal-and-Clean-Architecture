package kh.edu.istad.iam.persistence.adapter;

import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserPermission;
import kh.edu.istad.iam.domain.entity.UserRole;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.iam.persistence.entity.IdentityEntity;
import kh.edu.istad.iam.persistence.entity.UserEntity;
import kh.edu.istad.iam.persistence.entity.UserPermissionEntity;
import kh.edu.istad.iam.persistence.entity.UserRoleEntity;
import kh.edu.istad.iam.persistence.mapper.UserPersistenceMapper;
import kh.edu.istad.iam.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserPermissionJpaRepository userPermissionJpaRepository;
    private final IdentityJpaRepository identityJpaRepository;
    private final UserPersistenceMapper  userPersistenceMapper;
    @Override
    public Optional<User> findByUuid(UUID uuid) {
        return userJpaRepository.findByUuidAndIsDeletedFalse(uuid)
                .map(userPersistenceMapper::userToDomain);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userPersistenceMapper.userToEntity(user);
        UserEntity savedUserEntity = userJpaRepository.save(userEntity);
        return userPersistenceMapper.userToDomain(savedUserEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .filter(user -> !user.isDeleted())
                .map(userPersistenceMapper::userToDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsernameAndIsDeletedFalse(username)
                .map(userPersistenceMapper::userToDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmailAndIsDeletedFalse(email)
                .map(userPersistenceMapper::userToDomain);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userJpaRepository.findByUsernameOrEmail(usernameOrEmail)
                .map(userPersistenceMapper::userToDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsernameAndIsDeletedFalse(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmailAndIsDeletedFalse(email);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userJpaRepository.existsByPhoneNumberAndIsDeletedFalse(phoneNumber);
    }

    @Override
    public List<User> findAll(int page, int size, String sort) {
        Sort sosrtOrder = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sosrtOrder);
        return userJpaRepository.findAllByIsDeletedFalse(pageable)
                .stream().map(userPersistenceMapper::userToDomain)
                .toList();
    }

    @Override
    public long count() {
        return userJpaRepository.countByIsDeletedFalse();
    }

    @Override
    public UserRole saveUserRole(UserRole userRole) {
        UserRoleEntity userRoleEntity = userPersistenceMapper.userRoleToEntity(userRole);
        UserRoleEntity savedUserRoleEntity = userRoleJpaRepository.save(userRoleEntity);
        return userPersistenceMapper.entityToUserRole(savedUserRoleEntity);
    }

    @Override
    public List<UserRole> findRolesByUserId(Long userId) {
        return userRoleJpaRepository.findByUserIdAndRevokedAtIsNull(userId)
                .stream().map(userPersistenceMapper::entityToUserRole).toList();
    }

    @Override
    public Optional<UserRole> findActiveUserRole(Long userId, Long roleId) {
        return userRoleJpaRepository.findByUserIdAndRoleIdAndRevokedAtIsNull(userId,roleId)
                .map(userPersistenceMapper::entityToUserRole);
    }

    @Override
    public UserPermission saveUserPermission(UserPermission userPermission) {
        UserPermissionEntity userPermissionEntity = userPersistenceMapper.userPermissionToEntity(userPermission);
        UserPermissionEntity savedUserPermissionEntity = userPermissionJpaRepository.save(userPermissionEntity);
        return userPersistenceMapper.entityToUserPermission(savedUserPermissionEntity);
    }

    @Override
    public List<UserPermission> findPermissionsByUserId(Long userId) {
        return userPermissionJpaRepository.findByUserIdAndRevokedAtIsNull(userId)
                .stream().map(userPersistenceMapper::entityToUserPermission).toList();
    }

    @Override
    public Optional<UserPermission> findActiveUserPermission(Long userId, Long permissionId) {
        return userPermissionJpaRepository.findByUserIdAndPermissionIdAndRevokedAtIsNull(userId, permissionId)
                .map(userPersistenceMapper::entityToUserPermission);
    }

    @Override
    public Identity saveIdentity(Identity identity) {
        IdentityEntity identityEntity = userPersistenceMapper.identityToEntity(identity);
        IdentityEntity savedIdentityEntity = identityJpaRepository.save(identityEntity);
        return userPersistenceMapper.entityToIdentity(savedIdentityEntity);
    }

    @Override
    public Optional<Identity> findIdentityByUuid(UUID identityUuid) {
        return identityJpaRepository.findByUuidAndIsDeletedFalse(identityUuid)
                .map(userPersistenceMapper::entityToIdentity);
    }

    @Override
    public Optional<Identity> findIdentityByProviderAndSubject(IdentityProvider provider, String providerSubject) {
        return identityJpaRepository.findByProviderAndProviderSubjectAndIsDeletedFalse(provider.name(), providerSubject)
                .map(userPersistenceMapper::entityToIdentity);

    }

    @Override
    public List<Identity> findIdentitiesByUserId(Long userId) {
        return identityJpaRepository.findByUserIdAndIsDeletedFalse(userId)
                .stream().map(userPersistenceMapper::entityToIdentity).toList();
    }

    @Override
    public Optional<Identity> findIdentityByUserIdAndProvider(Long userId, IdentityProvider provider) {
        return identityJpaRepository.findByUserIdAndProviderAndIsDeletedFalse(userId,provider.name())
                .map(userPersistenceMapper::entityToIdentity);
    }
}
