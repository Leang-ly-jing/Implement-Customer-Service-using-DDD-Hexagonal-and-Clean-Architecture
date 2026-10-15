package kh.edu.istad.platform.application.port.out;

import kh.edu.istad.platform.domain.entity.Business;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BusinessRepository {
    Mono<Business> save(Business business);
    Mono<Business> findById(UUID businessId);
}
