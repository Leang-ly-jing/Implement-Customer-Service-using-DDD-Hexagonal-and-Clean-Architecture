package kh.edu.istad.platform.customer.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customers")
@Entity
public class CustomerEntity {
    @Id
    private UUID customerId;
    private String userName;
    private String familyName;
    private String givenName;
    private String email;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private CustomerStatus customerStatus;
}
