package kh.edu.istad.platform.order.persistence.repository;

import kh.edu.istad.platform.order.persistence.entity.OrderEntity;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface OrderJpaRepository extends ReactiveMongoRepository<OrderEntity, UUID> {
    Mono<OrderEntity> findByTrackingId(UUID trackingId);
}