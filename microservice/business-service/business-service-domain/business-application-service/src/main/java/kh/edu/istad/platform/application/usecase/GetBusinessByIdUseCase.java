package kh.edu.istad.platform.application.usecase;

import kh.edu.istad.platform.application.port.out.BusinessRepository;
import kh.edu.istad.platform.domain.entity.Business;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetBusinessByIdUseCase {
    private final BusinessRepository businessRepository;

    public Mono<Business> execute(UUID businessId) {
        return businessRepository.findById(businessId);
    }
}
