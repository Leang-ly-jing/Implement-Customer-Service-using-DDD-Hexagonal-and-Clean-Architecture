package kh.edu.istad.iam.domain.dto.command.user;

public record RegisterUserCommand(
        String username,
        String email,
        String password,
        String confirmPassword,
        String givenName,
        String familyName,
        String locale
) {}