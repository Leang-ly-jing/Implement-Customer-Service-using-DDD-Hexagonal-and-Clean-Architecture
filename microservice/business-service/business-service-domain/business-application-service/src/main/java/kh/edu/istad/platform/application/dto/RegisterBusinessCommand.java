package kh.edu.istad.platform.application.dto;

import java.util.List;

public record RegisterBusinessCommand(
        String legalName,
        String displayName,
        String email,
        String phoneNumber,
        List<ProductCommand> products
) {
}
