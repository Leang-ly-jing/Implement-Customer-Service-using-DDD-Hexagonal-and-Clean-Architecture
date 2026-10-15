package kh.edu.istad.platform.payment.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.CreditEntryId;
import kh.edu.istad.common.domain.valueobject.CreditHistoryId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.PaymentId;
import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.domain.entity.CreditHistory;
import kh.edu.istad.platform.domain.entity.Payment;
import kh.edu.istad.platform.payment.persistence.document.CreditEntryDocument;
import kh.edu.istad.platform.payment.persistence.document.CreditHistoryDocument;
import kh.edu.istad.platform.payment.persistence.document.PaymentDocument;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

@Component
public class PaymentPersistenceMapper {

    public PaymentDocument toPaymentDocument(Payment payment) {
        return PaymentDocument.builder()
                .id(payment.getId().value())
                .customerId(payment.getCustomerId().value())
                .orderId(payment.getOrderId().value())
                .price(payment.getPrice().amount())
                .paymentStatus(payment.getPaymentStatus())
                .createdAt(payment.getCreatedAt() != null ? payment.getCreatedAt().toInstant() : Instant.now())
                .build();
    }

    public Payment toPaymentDomain(PaymentDocument document) {
        return Payment.builder()
                .paymentId(new PaymentId(document.getId()))
                .customerId(new CustomerId(document.getCustomerId()))
                .orderId(new OrderId(document.getOrderId()))
                .price(new Money(document.getPrice()))
                .paymentStatus(document.getPaymentStatus())
                .createdAt(document.getCreatedAt() != null ? document.getCreatedAt().atZone(ZoneId.of("UTC")) : ZonedDateTime.now(ZoneId.of("UTC")))
                .build();
    }

    public CreditEntryDocument toCreditEntryDocument(CreditEntry creditEntry) {
        return CreditEntryDocument.builder()
                .id(creditEntry.getId().value())
                .customerId(creditEntry.getCustomerId().value())
                .totalCreditAmount(creditEntry.getTotalCreditAmount().amount())
                .build();
    }

    public CreditEntry toCreditEntryDomain(CreditEntryDocument document) {
        return CreditEntry.builder()
                .creditEntryId(new CreditEntryId(document.getId()))
                .customerId(new CustomerId(document.getCustomerId()))
                .totalCreditAmount(new Money(document.getTotalCreditAmount()))
                .build();
    }

    public CreditHistoryDocument toCreditHistoryDocument(CreditHistory creditHistory) {
        return CreditHistoryDocument.builder()
                .id(creditHistory.getId().value())
                .customerId(creditHistory.getCustomerId().value())
                .amount(creditHistory.getAmount().amount())
                .transactionType(creditHistory.getTransactionType())
                .build();
    }

    public CreditHistory toCreditHistoryDomain(CreditHistoryDocument document) {
        return CreditHistory.builder()
                .creditHistoryId(new CreditHistoryId(document.getId()))
                .customerId(new CustomerId(document.getCustomerId()))
                .amount(new Money(document.getAmount()))
                .transactionType(document.getTransactionType())
                .build();
    }
}
