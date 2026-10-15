package kh.edu.istad.platform.application.dto;

import java.util.UUID;

public record RegisterBusinessResult(
        UUID businessId,
        String legalName,
        String displayName,
        boolean active,
        String message
) {
}
