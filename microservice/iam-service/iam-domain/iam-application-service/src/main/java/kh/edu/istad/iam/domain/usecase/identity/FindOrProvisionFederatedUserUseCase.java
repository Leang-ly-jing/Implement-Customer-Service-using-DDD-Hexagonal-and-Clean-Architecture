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
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FindOrProvisionFederatedUserUseCase {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase;
    private final IdentityDataMapper identityDataMapper;

    public AuthUserResult execute(FederatedIdentityCommand command) {
        Instant now = Instant.now();
        Optional<Identity> existingIdentity = userRepository.findIdentityByProviderAndSubject(
                command.provider(), command.providerSubject());

        if (existingIdentity.isPresent()) {
            Identity identity = existingIdentity.get();
            identity.setLastLoginAt(now);
            userRepository.saveIdentity(identity);

            User user = userRepository.findById(identity.getUserId())
                    .orElseThrow(() -> new AuthorizationDomainException("Associated user not found"));
            return loadUserForAuthenticationUseCase.execute(user.getUsernameValue());
        }

        User targetUser;
        Optional<User> existingUserByEmail = (command.email() != null && !command.email().isBlank())
                ? userRepository.findByEmail(command.email())
                : Optional.empty();

        if (existingUserByEmail.isPresent()) {
            targetUser = existingUserByEmail.get();
            targetUser.setLastLoginAt(now);
            targetUser.setUpdatedAt(now);
            targetUser.setUpdatedBy("FEDERATION");
            userRepository.save(targetUser);
        } else {
            String rawUsername = command.username() != null ? command.username().trim() : "user_" + UUID.randomUUID().toString().substring(0, 8);
            String username = rawUsername.replaceAll("\\s+", "_");
            while (userRepository.existsByUsername(username)) {
                username = "user_" + UUID.randomUUID().toString().substring(0, 8);
            }

            String cleanEmailUsername = username.replaceAll("[^a-zA-Z0-9._-]", "");
            if (cleanEmailUsername.isBlank()) {
                cleanEmailUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
            }

            String email = (command.email() != null && !command.email().isBlank())
                    ? command.email().trim()
                    : cleanEmailUsername + "@social.istad.co";

            String rawName = command.displayName() != null && !command.displayName().isBlank()
                    ? command.displayName().trim()
                    : "Social User";

            String familyName = "User";
            String givenName = rawName;

            if (rawName.contains(" ")) {
                int lastSpace = rawName.lastIndexOf(" ");
                familyName = rawName.substring(0, lastSpace).trim();
                givenName = rawName.substring(lastSpace + 1).trim();
            }

            User newUser = User.builder()
                    .uuid(UUID.randomUUID())
                    .username(username)
                    .email(email)
                    .emailVerified(command.emailVerified())
                    .passwordHash(null)
                    .givenName(givenName.isBlank() ? "Social" : givenName)
                    .familyName(familyName.isBlank() ? "User" : familyName)
                    .locale("km")
                    .pictureUrl(command.pictureUrl())
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .failedLoginAttempts(0)
                    .lastLoginAt(now)
                    .createdAt(now)
                    .createdBy("FEDERATION")
                    .updatedAt(now)
                    .updatedBy("FEDERATION")
                    .version(0L)
                    .build();

            targetUser = userRepository.save(newUser);

            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new AuthorizationDomainException("Default role USER not found"));

            UserRole ur = UserRole.builder()
                    .userId(targetUser.getId())
                    .roleId(userRole.getId())
                    .assignedAt(now)
                    .assignedBy("FEDERATION")
                    .reason("Social user provisioning")
                    .build();
            userRepository.saveUserRole(ur);
        }

        Identity identity = identityDataMapper.federatedCommandToIdentity(targetUser.getId(), command, "FEDERATION", now);
        userRepository.saveIdentity(identity);

        return loadUserForAuthenticationUseCase.execute(targetUser.getUsernameValue());
    }
}
