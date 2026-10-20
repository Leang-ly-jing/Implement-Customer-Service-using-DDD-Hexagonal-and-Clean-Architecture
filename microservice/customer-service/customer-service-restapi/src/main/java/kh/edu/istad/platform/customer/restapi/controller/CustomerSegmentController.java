package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.usecase.CreateCustomerSegmentUseCase;
import kh.edu.istad.platform.customer.domain.usecase.DeleteCustomerSegmentUseCase;
import kh.edu.istad.platform.customer.domain.usecase.QueryCustomerSegmentUseCase;
import kh.edu.istad.platform.customer.domain.usecase.UpdateCustomerSegmentUseCase;
import kh.edu.istad.platform.customer.restapi.dto.CustomerSegmentCreateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerSegmentUpdateRequest;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerSegmentWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer-segments")
public class CustomerSegmentController {
    private final CreateCustomerSegmentUseCase createCustomerSegmentUseCase;
    private final QueryCustomerSegmentUseCase queryCustomerSegmentUseCase;
    private final UpdateCustomerSegmentUseCase updateCustomerSegmentUseCase;
    private final DeleteCustomerSegmentUseCase deleteCustomerSegmentUseCase;
    private final CustomerSegmentWebMapper customerSegmentWebMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<CustomerSegment> createCustomerSegment(
            @Valid @RequestBody CustomerSegmentCreateRequest request
    ) {
        return createCustomerSegmentUseCase.create(customerSegmentWebMapper.toDomain(request));
    }

    @GetMapping
    public Flux<CustomerSegment> findAll() {
        return queryCustomerSegmentUseCase.findAll();
    }

    @GetMapping("/{id}")
    public Mono<CustomerSegment> findById(@PathVariable("id") UUID id) {
        return queryCustomerSegmentUseCase.findById(new CustomerSegmentId(id));
    }

    @PutMapping("/{id}")
    public Mono<CustomerSegment> updateCustomerSegment(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CustomerSegmentUpdateRequest request
    ) {
        return updateCustomerSegmentUseCase.update(id, request.segmentLevel());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomerSegment(@PathVariable("id") UUID id) {
        return deleteCustomerSegmentUseCase.deleteById(id);
    }
}
