package kh.edu.istad.iam.restapi.dto.request;

public record UpdateUserProfileRequest(
        String givenName,
        String familyName,
        String nickname,
        String gender,
        String phoneNumber,
        String locale
) {
}
