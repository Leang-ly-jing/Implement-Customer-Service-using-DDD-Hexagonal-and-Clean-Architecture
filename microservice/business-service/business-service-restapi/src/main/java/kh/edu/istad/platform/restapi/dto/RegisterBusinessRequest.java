package kh.edu.istad.platform.restapi.dto;

import java.util.List;

public record RegisterBusinessRequest(
        String legalName,
        String displayName,
        String email,
        String phoneNumber,
        List<ProductRequest> products
) {}

