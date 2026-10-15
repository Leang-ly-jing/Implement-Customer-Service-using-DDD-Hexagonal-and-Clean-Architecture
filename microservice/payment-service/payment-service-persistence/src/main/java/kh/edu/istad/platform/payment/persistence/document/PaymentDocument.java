package kh.edu.istad.platform.payment.persistence.document;

import kh.edu.istad.common.domain.valueobject.PaymentStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class PaymentDocument {
    @Id
    private UUID id;
    private UUID orderId;
    private UUID customerId;
    private BigDecimal price;
    private PaymentStatus paymentStatus;
    private Instant createdAt;
}
