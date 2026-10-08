package kh.edu.istad.platform.customer.domain.dto;

import java.util.UUID;

public record UpdateCustomerCommand(
        UUID id,
        String familyName,
        String givenName
) {
}
