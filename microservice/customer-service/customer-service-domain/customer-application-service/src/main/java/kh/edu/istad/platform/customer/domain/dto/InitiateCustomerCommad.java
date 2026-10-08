package kh.edu.istad.platform.customer.domain.dto;

public record InitiateCustomerCommad(
        String userName,
        String familyName,
        String givenName,
        String email,
        String phoneNumber
) {
}
