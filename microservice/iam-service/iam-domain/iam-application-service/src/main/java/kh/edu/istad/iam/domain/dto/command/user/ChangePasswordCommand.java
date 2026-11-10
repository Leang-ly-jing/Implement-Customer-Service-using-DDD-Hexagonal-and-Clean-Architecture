package kh.edu.istad.iam.domain.dto.command.user;

import java.util.UUID;

public record ChangePasswordCommand(
        UUID userUuid,
        String currentPassword,
        String newPassword,
        String confirmPassword
) {}