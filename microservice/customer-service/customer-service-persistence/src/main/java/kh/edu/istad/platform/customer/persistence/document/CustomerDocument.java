package kh.edu.istad.platform.customer.persistence.document;


import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class CustomerDocument implements Persistable<String>{
    @Id
    private String id;
    private String userName;
    private String familyName;
    private String givenName;
    private String email;
    private String phoneNumber;
    private CustomerStatus customerStatus;
    @DocumentReference
    private CustomerSegmentDocument segment;
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private Instant createAt;
    @LastModifiedBy
    private String updateBy;
    @LastModifiedDate
    private String updateAt;

    @Override
    public boolean isNew() {
        return createAt == null;
    }
}
