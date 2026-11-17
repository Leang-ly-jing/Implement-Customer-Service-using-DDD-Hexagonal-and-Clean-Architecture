package kh.edu.istad.iam.domain.usecase.identity;

import kh.edu.istad.iam.domain.dto.command.identity.FederatedIdentityCommand;
import kh.edu.istad.iam.domain.dto.result.AuthUserResult;
import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserRole;
import kh.edu.istad.iam.domain.exception.AuthorizationDomainException;
import kh.edu.istad.iam.domain.mapper.IdentityDataMapper;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.usecase.user.LoadUserForAuthenticationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindOrProvisionFederatedUserUseCase {

    private static final String DEFAULT_ROLE_NAME = "USER";
    private static final String SYSTEM_ACTOR = "FEDERATION";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase;
    private final IdentityDataMapper identityDataMapper;

    public AuthUserResult execute(FederatedIdentityCommand command) {
        Instant now = Instant.now();

        // 1. Check if identity already exists across any state (active or soft-deleted)
        Optional<Identity> existingIdentityOpt = userRepository.findAnyIdentityByProviderAndSubject(
                command.provider(), command.providerSubject());

        if (existingIdentityOpt.isPresent()) {
            Identity identity = existingIdentityOpt.get();
            syncIdentity(identity, command, now);

            Optional<User> associatedUser = userRepository.findById(identity.getUserId());
            if (associatedUser.isPresent()) {
                User user = associatedUser.get();
                user.setLastLoginAt(now);
                userRepository.save(user);
                userRepository.saveIdentity(identity);
                return loadUserForAuthenticationUseCase.execute(user.getUsernameValue());
            }
        }

        // 2. Identify or provision the corresponding User
        User targetUser = findOrProvisionUser(command, now);

        // 3. Link or update the federated identity to the target user
        Identity identity;
        if (existingIdentityOpt.isPresent()) {
            identity = existingIdentityOpt.get();
            identity.setUserId(targetUser.getId());
            syncIdentity(identity, command, now);
        } else {
            identity = identityDataMapper.federatedCommandToIdentity(targetUser.getId(), command, SYSTEM_ACTOR, now);
        }

        userRepository.saveIdentity(identity);

        return loadUserForAuthenticationUseCase.execute(targetUser.getUsernameValue());
    }

    private void syncIdentity(Identity identity, FederatedIdentityCommand command, Instant now) {
        identity.setLastLoginAt(now);
        identity.setUpdatedAt(now);
        identity.setUpdatedBy(SYSTEM_ACTOR);

        // Restore if previously unlinked or soft-deleted
        if (identity.isDeleted()) {
            identity.setDeleted(false);
            identity.setDeletedAt(null);
            identity.setDeletedBy(null);
        }

        if (command.email() != null && !command.email().isBlank()) {
            identity.setEmail(command.email().trim());
            identity.setEmailVerified(command.emailVerified());
        }
        if (command.displayName() != null && !command.displayName().isBlank()) {
            identity.setDisplayName(command.displayName().trim());
        }
        if (command.username() != null && !command.username().isBlank()) {
            identity.setUsername(command.username().trim());
        }
        if (command.pictureUrl() != null && !command.pictureUrl().isBlank()) {
            identity.setPictureUrl(command.pictureUrl().trim());
        }
    }

    private User findOrProvisionUser(FederatedIdentityCommand command, Instant now) {
        if (command.email() != null && !command.email().isBlank()) {
            Optional<User> existingUser = userRepository.findByEmail(command.email().trim());
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setLastLoginAt(now);
                user.setUpdatedAt(now);
                user.setUpdatedBy(SYSTEM_ACTOR);
                return userRepository.save(user);
            }
        }
        return provisionNewUser(command, now);
    }

    private User provisionNewUser(FederatedIdentityCommand command, Instant now) {
        String username = generateUniqueUsername(command.username());
        String email = resolveEmail(command.email(), username);
        String[] nameParts = parseName(command.displayName());

        User newUser = User.builder()
                .uuid(UUID.randomUUID())
                .username(username)
                .email(email)
                .emailVerified(command.emailVerified())
                .passwordHash(null)
                .givenName(nameParts[0])
                .familyName(nameParts[1])
                .locale("km")
                .pictureUrl(command.pictureUrl())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .failedLoginAttempts(0)
                .lastLoginAt(now)
                .createdAt(now)
                .createdBy(SYSTEM_ACTOR)
                .updatedAt(now)
                .updatedBy(SYSTEM_ACTOR)
                .version(0L)
                .build();

        User savedUser = userRepository.save(newUser);

        Role defaultRole = roleRepository.findByName(DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new AuthorizationDomainException("Default role " + DEFAULT_ROLE_NAME + " not found"));

        UserRole userRole = UserRole.builder()
                .userId(savedUser.getId())
                .roleId(defaultRole.getId())
                .assignedAt(now)
                .assignedBy(SYSTEM_ACTOR)
                .reason("Social user provisioning")
                .build();

        userRepository.saveUserRole(userRole);

        return savedUser;
    }

    private String generateUniqueUsername(String rawUsername) {
        String base = (rawUsername != null && !rawUsername.isBlank())
                ? rawUsername.trim().replaceAll("\\s+", "_")
                : "user_" + UUID.randomUUID().toString().substring(0, 8);

        String candidate = base;
        while (userRepository.existsByUsername(candidate)) {
            candidate = "user_" + UUID.randomUUID().toString().substring(0, 8);
        }
        return candidate;
    }

    private String resolveEmail(String rawEmail, String username) {
        if (rawEmail != null && !rawEmail.isBlank()) {
            return rawEmail.trim();
        }
        String cleanPrefix = username.replaceAll("[^a-zA-Z0-9._-]", "");
        if (cleanPrefix.isBlank()) {
            cleanPrefix = "user_" + UUID.randomUUID().toString().substring(0, 8);
        }
        return cleanPrefix + "@social.istad.co";
    }

    private String[] parseName(String rawDisplayName) {
        if (rawDisplayName == null || rawDisplayName.isBlank()) {
            return new String[]{"Social", "User"};
        }
        String trimmed = rawDisplayName.trim();
        int lastSpace = trimmed.lastIndexOf(' ');
        if (lastSpace > 0) {
            String family = trimmed.substring(0, lastSpace).trim();
            String given = trimmed.substring(lastSpace + 1).trim();
            return new String[]{given.isBlank() ? "Social" : given, family.isBlank() ? "User" : family};
        }
        return new String[]{trimmed, "User"};
    }
}
