package kh.edu.istad.platform.order.restapi.dto;

public record OrderAddressRequest(
        String street,
        String postalCode,
        String city
) {
}
