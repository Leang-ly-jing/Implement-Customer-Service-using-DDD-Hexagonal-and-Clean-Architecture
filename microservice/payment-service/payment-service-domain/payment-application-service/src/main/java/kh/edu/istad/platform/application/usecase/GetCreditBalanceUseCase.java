package kh.edu.istad.platform.application.usecase;

import kh.edu.istad.platform.application.dto.CreditBalanceResult;
import kh.edu.istad.platform.application.mapper.PaymentApplicationMapper;
import kh.edu.istad.platform.application.port.out.CreditEntryRepository;
import kh.edu.istad.platform.domain.exception.PaymentDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetCreditBalanceUseCase {

    private final CreditEntryRepository creditEntryRepository;
    private final PaymentApplicationMapper mapper;

    public Mono<CreditBalanceResult> execute(UUID customerId) {
        return creditEntryRepository.findByCustomerId(customerId)
                .switchIfEmpty(Mono.error(new PaymentDomainException("Credit entry not found for customer: " + customerId)))
                .map(mapper::toCreditBalanceResult);
    }
}
