package kh.edu.istad.platform.customer.persistence.document;

import org.springframework.data.annotation.Id;
import kh.edu.istad.common.domain.valueobject.CustomerSegmentLevel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers_segment")
public class CustomerSegmentDocument {
    @Id
    private String id;
    private CustomerSegmentLevel segmentLevel;
}
