package kh.edu.istad.iam.web.dto;

import java.util.Set;

public record ConnectedAppDto(
        String clientId,
        String clientName,
        Set<String> scopes
) {}

