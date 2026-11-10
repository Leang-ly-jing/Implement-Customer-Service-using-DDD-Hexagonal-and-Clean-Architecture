package kh.edu.istad.iam.domain.dto.command.user;

import kh.edu.istad.iam.domain.valueobject.Address;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record CreateUserCommand(
        String username,
        String email,
        String password,
        String givenName,
        String familyName,
        String gender,
        LocalDate birthdate,
        String phoneNumber,
        String zoneinfo,
        String locale,
        Address address,
        Set<UUID> roleUuids
) {}