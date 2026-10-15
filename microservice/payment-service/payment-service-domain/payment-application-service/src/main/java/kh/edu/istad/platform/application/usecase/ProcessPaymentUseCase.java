package kh.edu.istad.platform.application.usecase;

import kh.edu.istad.common.domain.valueobject.PaymentStatus;
import kh.edu.istad.platform.application.dto.ProcessPaymentCommand;
import kh.edu.istad.platform.application.dto.ProcessPaymentResult;
import kh.edu.istad.platform.application.mapper.PaymentApplicationMapper;
import kh.edu.istad.platform.application.port.out.CreditEntryRepository;
import kh.edu.istad.platform.application.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.application.port.out.PaymentRepository;
import kh.edu.istad.platform.domain.entity.CreditEntry;
import kh.edu.istad.platform.domain.entity.CreditHistory;
import kh.edu.istad.platform.domain.entity.Payment;
import kh.edu.istad.platform.domain.event.PaymentEvent;
import kh.edu.istad.platform.domain.exception.PaymentDomainException;
import kh.edu.istad.platform.domain.service.PaymentDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessPaymentUseCase {

    private final PaymentDomainService paymentDomainService;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentApplicationMapper mapper;

    public Mono<ProcessPaymentResult> execute(ProcessPaymentCommand command) {
        Payment payment = mapper.toPayment(command);

        return creditEntryRepository.findByCustomerId(command.customerId())
                .switchIfEmpty(Mono.error(new PaymentDomainException("Could not find credit entry for customer: " + command.customerId())))
                .flatMap(creditEntry ->
                        creditHistoryRepository.findByCustomerId(command.customerId())
                                .collectList()
                                .flatMap(creditHistories -> {
                                    List<String> failureMessages = new ArrayList<>();
                                    int initialHistoryCount = creditHistories.size();

                                    PaymentEvent paymentEvent = paymentDomainService.processPayment(
                                            payment,
                                            creditEntry,
                                            creditHistories,
                                            failureMessages
                                    );

                                    return paymentRepository.save(paymentEvent.getPayment())
                                            .flatMap(savedPayment -> {
                                                if (savedPayment.getPaymentStatus() == PaymentStatus.COMPLETED) {
                                                    List<CreditHistory> newHistories = creditHistories.subList(initialHistoryCount, creditHistories.size());

                                                    return creditEntryRepository.save(creditEntry)
                                                            .thenMany(Flux.fromIterable(newHistories))
                                                            .flatMap(creditHistoryRepository::save)
                                                            .then(Mono.just(mapper.toResult(savedPayment, failureMessages)));
                                                }
                                                return Mono.just(mapper.toResult(savedPayment, failureMessages));
                                            });
                                })
                );
    }
}
