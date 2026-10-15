package kh.edu.istad.platform.payment.persistence.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "credit_entries")
public class CreditEntryDocument {
    @Id
    private UUID id;
    @Indexed
    private UUID customerId;
    private BigDecimal totalCreditAmount;
}
