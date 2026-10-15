package kh.edu.istad.platform.restapi.dto;

import java.util.UUID;
public record RegisterBusinessResponse(
        UUID businessId,
        String legalName,
        String displayName,
        boolean active,
        String message
) {}