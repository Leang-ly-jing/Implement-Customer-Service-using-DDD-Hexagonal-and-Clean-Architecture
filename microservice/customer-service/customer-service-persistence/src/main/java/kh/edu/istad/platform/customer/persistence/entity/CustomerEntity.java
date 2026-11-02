package kh.edu.istad.platform.customer.persistence.entity;


import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class CustomerEntity {
    @Id
    private UUID customerId;
    private String userName;
    private String familyName;
    private String givenName;
    private String email;
    private String phoneNumber;

    private CustomerStatus customerStatus;
}
