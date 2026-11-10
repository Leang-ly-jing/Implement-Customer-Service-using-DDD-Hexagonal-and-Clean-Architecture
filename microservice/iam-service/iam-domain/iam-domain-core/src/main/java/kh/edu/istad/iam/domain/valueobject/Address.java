package kh.edu.istad.iam.domain.valueobject;

public record Address(
        String streetAddress,
        String locality,
        String region,
        String postalCode,
        String country
) {}