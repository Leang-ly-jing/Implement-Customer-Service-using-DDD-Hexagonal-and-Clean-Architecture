package kh.edu.istad.iam.domain.mapper;

import kh.edu.istad.iam.domain.dto.command.user.CreateUserCommand;
import kh.edu.istad.iam.domain.dto.command.user.RegisterUserCommand;
import kh.edu.istad.iam.domain.dto.result.AuthUserResult;
import kh.edu.istad.iam.domain.dto.result.RoleSummary;
import kh.edu.istad.iam.domain.dto.result.UserResult;
import kh.edu.istad.iam.domain.dto.result.UserSummary;
import kh.edu.istad.iam.domain.entity.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
@Component
public class UserDataMapper {
    public User registerCommandToUser(RegisterUserCommand command,
                                      String passwordHash, Instant now){
        return User.builder()
                .uuid(UUID.randomUUID())
                .username(command.username())
                .email(command.email())
                .emailVerified(false)
                .passwordHash(passwordHash)
                .givenName(command.givenName())
                .familyName(command.familyName())
                .locale(command.locale() !=null ? command.locale() : "kh")
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .failedLoginAttempts(0)
                .createdAt(now)
                .createdBy(command.username())
                .updatedAt(now)
                .updatedBy(command.username())
                .version(0L)
                .build();
    }

    public User createCommandToUser(CreateUserCommand command,String passwordHash, String actor, Instant now){
        return User.builder()
                .uuid(UUID.randomUUID())
                .username(command.username())
                .email(command.email())
                .emailVerified(false)
                .passwordHash(passwordHash)
                .givenName(command.givenName())
                .familyName(command.familyName())
                .gender(command.gender())
                .birthdate(command.birthdate())
                .phoneNumber(command.phoneNumber())
                .phoneNumberVerified(false)
                .zoneinfo(command.zoneinfo())
                .locale(command.locale() != null ? command.locale() : "kh")
                .address(command.address())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .failedLoginAttempts(0)
                .createdAt(now)
                .createdBy(actor)
                .updatedAt(now)
                .updatedBy(actor)
                .version(0L)
                .build();
    }
    public UserResult userToUserResult(User user, List<RoleSummary> roles) {
        return new UserResult(
                user.getUuid(),
                user.getUsernameValue(),
                user.getEmailValue(),
                user.isEmailVerified(),
                user.getFullName(),
                user.getLocale(),
                user.isEnabled(),
                roles != null ? roles : List.of(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
    public UserSummary userToUserSummary(User user) {
        return new UserSummary(
                user.getUuid(),
                user.getUsernameValue(),
                user.getEmailValue(),
                user.getFullName(),
                user.isEnabled()
        );
    }
    public AuthUserResult userToAuthUserResult(User user, Set<String> authorities) {
        return new AuthUserResult(
                user.getId(),
                user.getUuid(),
                user.getUsernameValue(),
                user.getEmailValue(),
                user.getPasswordHash(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                authorities
        );
    }
}
