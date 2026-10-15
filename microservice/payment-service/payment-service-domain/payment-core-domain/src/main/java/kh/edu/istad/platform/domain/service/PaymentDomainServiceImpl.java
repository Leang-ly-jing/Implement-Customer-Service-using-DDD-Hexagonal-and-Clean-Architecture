package kh.edu.istad.platform.domain.service;

import kh.edu.istad.common.domain.valueobject.CreditHistoryId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.PaymentStatus;
import kh.edu.istad.common.domain.valueobject.TransactionType;
import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.domain.entity.CreditHistory;
import kh.edu.istad.platform.domain.entity.Payment;
import kh.edu.istad.platform.domain.event.PaymentCancelledEvent;
import kh.edu.istad.platform.domain.event.PaymentCompletedEvent;
import kh.edu.istad.platform.domain.event.PaymentEvent;
import kh.edu.istad.platform.domain.event.PaymentFailedEvent;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentDomainServiceImpl implements PaymentDomainService {

    @Override
    public PaymentEvent processPayment(Payment payment, CreditEntry entry, List<CreditHistory> histories, List<String> failures) {
        payment.validatePayment(failures);
        payment.initializePayment();
        validateCredit(payment, entry, failures);
        validateHistory(entry, histories, failures);

        if (failures.isEmpty()) {
            entry.subtractCreditAmount(payment.getPrice());

            CreditHistory debitHistory = CreditHistory.builder()
                    .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                    .customerId(payment.getCustomerId())
                    .amount(payment.getPrice())
                    .transactionType(TransactionType.DEBIT)
                    .build();
            histories.add(debitHistory);

            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failures);
        } else {
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failures);
        }
    }

    @Override
    public PaymentCancelledEvent cancelPayment(Payment payment, CreditEntry entry) {
        payment.validatePayment(new ArrayList<>());
        entry.addCreditAmount(payment.getPrice());
        payment.updateStatus(PaymentStatus.CANCELLED);
        return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), new ArrayList<>());
    }

    @Override
    public void validateCredit(Payment payment, CreditEntry entry, List<String> failures) {
        if (payment.getPrice().amount().compareTo(entry.getTotalCreditAmount().amount()) > 0) {
            failures.add("Customer does not have enough credit for payment!");
        }
    }

    @Override
    public void validateHistory(CreditEntry entry, List<CreditHistory> histories, List<String> failures) {
        Money totalCredit = sum(histories, TransactionType.CREDIT);
        Money totalDebit = sum(histories, TransactionType.DEBIT);

        if (totalDebit.amount().compareTo(totalCredit.amount()) > 0) {
            failures.add("Customer total debit is greater than total credit in history!");
        }

        BigDecimal historyBalance = totalCredit.amount().subtract(totalDebit.amount());
        if (entry.getTotalCreditAmount().amount().compareTo(historyBalance) != 0) {
            failures.add("Credit entry total is not equal to credit history balance!");
        }
    }

    @Override
    public Money sum(List<CreditHistory> histories, TransactionType type) {
        BigDecimal total = histories.stream()
                .filter(history -> history.getTransactionType() == type)
                .map(history -> history.getAmount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Money(total);
    }
}
