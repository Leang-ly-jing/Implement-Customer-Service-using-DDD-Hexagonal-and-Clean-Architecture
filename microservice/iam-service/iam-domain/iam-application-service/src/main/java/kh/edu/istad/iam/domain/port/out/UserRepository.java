package kh.edu.istad.iam.domain.port.out;

import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserPermission;
import kh.edu.istad.iam.domain.entity.UserRole;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    List<User> findAll(int page, int size, String sort);

    long count();

    UserRole saveUserRole(UserRole userRole);

    List<UserRole> findRolesByUserId(Long userId);

    Optional<UserRole> findActiveUserRole(Long userId, Long roleId);

    UserPermission saveUserPermission(UserPermission userPermission);

    List<UserPermission> findPermissionsByUserId(Long userId);

    Optional<UserPermission> findActiveUserPermission(Long userId, Long permissionId);

    Identity saveIdentity(Identity identity);

    Optional<Identity> findIdentityByUuid(UUID identityUuid);

    Optional<Identity> findIdentityByProviderAndSubject(IdentityProvider provider, String providerSubject);

    List<Identity> findIdentitiesByUserId(Long userId);

    Optional<Identity> findIdentityByUserIdAndProvider(Long userId, IdentityProvider provider);
}