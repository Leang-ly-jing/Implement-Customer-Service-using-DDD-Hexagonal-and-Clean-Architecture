package kh.edu.istad.platform.application.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.application.dto.CreditBalanceResult;
import kh.edu.istad.platform.application.dto.ProcessPaymentCommand;
import kh.edu.istad.platform.application.dto.ProcessPaymentResult;
import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.domain.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentApplicationMapper {

    public Payment toPayment(ProcessPaymentCommand command) {
        return Payment.builder()
                .customerId(new CustomerId(command.customerId()))
                .orderId(new OrderId(command.orderId()))
                .price(new Money(command.price()))
                .build();
    }

    public ProcessPaymentResult toResult(Payment payment, List<String> failureMessages) {
        return new ProcessPaymentResult(
                payment.getId() != null ? payment.getId().value() : null,
                payment.getCustomerId().value(),
                payment.getOrderId().value(),
                payment.getPrice().amount(),
                payment.getPaymentStatus(),
                payment.getCreatedAt(),
                failureMessages
        );
    }

    public CreditBalanceResult toCreditBalanceResult(CreditEntry creditEntry) {
        return new CreditBalanceResult(
                creditEntry.getCustomerId().value(),
                creditEntry.getTotalCreditAmount().amount()
        );
    }
}
