package kh.edu.istad.iam.domain.dto.command.user;

import java.util.UUID;

public record UpdateUserProfileCommand(
        UUID userUuid,
        String givenName,
        String familyName,
        String middleName,
        String nickname,
        String phoneNumber,
        String locale
) {}
