package kh.edu.istad.platform.application.usecase;

import kh.edu.istad.common.domain.valueobject.CreditEntryId;
import kh.edu.istad.common.domain.valueobject.CreditHistoryId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.TransactionType;
import kh.edu.istad.platform.application.dto.CreateCreditEntryCommand;
import kh.edu.istad.platform.application.dto.CreditBalanceResult;
import kh.edu.istad.platform.application.mapper.PaymentApplicationMapper;
import kh.edu.istad.platform.application.port.out.CreditEntryRepository;
import kh.edu.istad.platform.application.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.domain.entity.CreditHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateCreditEntryUseCase {

    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentApplicationMapper mapper;

    public Mono<CreditBalanceResult> execute(CreateCreditEntryCommand command) {
        CustomerId customerId = new CustomerId(command.customerId());
        Money amount = new Money(command.totalCreditAmount());

        CreditEntry creditEntry = CreditEntry.builder()
                .creditEntryId(new CreditEntryId(UUID.randomUUID()))
                .customerId(customerId)
                .totalCreditAmount(amount)
                .build();

        CreditHistory creditHistory = CreditHistory.builder()
                .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                .customerId(customerId)
                .amount(amount)
                .transactionType(TransactionType.CREDIT)
                .build();

        return creditEntryRepository.save(creditEntry)
                .flatMap(savedEntry -> creditHistoryRepository.save(creditHistory)
                        .thenReturn(mapper.toCreditBalanceResult(savedEntry)));
    }
}
