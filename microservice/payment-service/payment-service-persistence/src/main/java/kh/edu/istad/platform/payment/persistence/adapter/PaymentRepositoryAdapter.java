package kh.edu.istad.platform.payment.persistence.adapter;

import kh.edu.istad.platform.application.port.out.PaymentRepository;
import kh.edu.istad.platform.domain.entity.Payment;
import kh.edu.istad.platform.payment.persistence.mapper.PaymentPersistenceMapper;
import kh.edu.istad.platform.payment.persistence.repository.PaymentReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentReactiveMongoRepository mongoRepository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public Mono<Payment> save(Payment payment) {
        return mongoRepository.save(mapper.toPaymentDocument(payment))
                .map(mapper::toPaymentDomain);
    }

    @Override
    public Mono<Payment> findByOrderId(UUID orderId) {
        return mongoRepository.findByOrderId(orderId)
                .map(mapper::toPaymentDomain);
    }
}
