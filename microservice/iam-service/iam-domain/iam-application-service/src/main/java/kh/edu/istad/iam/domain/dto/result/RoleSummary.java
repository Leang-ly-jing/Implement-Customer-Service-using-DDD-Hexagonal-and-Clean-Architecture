package kh.edu.istad.iam.domain.dto.result;

import java.util.UUID;

public record RoleSummary(
        UUID uuid,
        String name,
        String description,
        boolean system,
        boolean active
) {
    public RoleSummary(UUID uuid, String name, boolean active) {
        this(uuid, name, null, false, active);
    }
}
