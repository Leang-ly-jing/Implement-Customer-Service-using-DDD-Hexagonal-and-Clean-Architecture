package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatistic;
import kh.edu.istad.platform.customer.domain.dto.Filtering;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.usecase.CreateCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.DeactivateCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.QueryCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.UpdateCustomerUseCase;
import kh.edu.istad.platform.customer.restapi.dto.CustomerCreateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateRequest;
import kh.edu.istad.platform.customer.restapi.dto.PaginatedResponse;
import kh.edu.istad.platform.customer.restapi.filter.FilteringFactory;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {
    private final CreateCustomerUseCase createCustomerUseCase;
    private final QueryCustomerUseCase queryCustomerUseCase;
    private final CustomerWebMapper customerWebMapper;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeactivateCustomerUseCase deactivateCustomerUseCase;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Customer> createCustomer(
            @Valid @RequestBody CustomerCreateRequest request
    ) {
        Customer customer = customerWebMapper.toDomain(request);
        CustomerSegmentId segmentId = new CustomerSegmentId(request.segmentId());
        return createCustomerUseCase.create(customer, segmentId);
    }

    @GetMapping
    public Flux<Customer> findAll(){
        return queryCustomerUseCase.findAll();
    }

    @GetMapping("/statistics")
    public Flux<CustomerStatistic> getStatistics() {
        return queryCustomerUseCase.getStatistics();
    }

    @GetMapping("/{id}")
    public Mono<Customer> findById(@PathVariable("id") UUID id){
        return queryCustomerUseCase.findById(new CustomerId(id));
    }

    @PutMapping("/{id}")
    public Mono<UpdateCustomerResult> updateCustomer(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
        UpdateCustomerCommand command = new UpdateCustomerCommand(
                id,
                request.familyName(),
                request.givenName()
        );
        return updateCustomerUseCase.execute(command);
    }


    @PatchMapping("/{id}/deactivate")
    public Mono<Customer> deactivateCustomer(@PathVariable("id") UUID id) {
        return deactivateCustomerUseCase.deactivate(id);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable("id") UUID id) {
        return deactivateCustomerUseCase.deleteById(id);
    }


    @GetMapping("/filter")
    public Mono<PaginatedResponse<Customer>> getCustomersWithFilter(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "filter", required = false) List<String> filter
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Filtering filtering = FilteringFactory.parseFromParams(filter, Customer.class);
        return queryCustomerUseCase.findWithFilter(filtering, pageable)
                .map(all -> PaginatedResponse.<Customer>builder()
                        .currentPage(all.getNumber())
                        .pageSize(all.getSize())
                        .totalItems(all.getTotalElements())
                        .totalPages(all.getTotalPages())
                        .items(all.getContent())
                        .hasNext(all.hasNext())
                        .build());
    }




    @GetMapping("/query")
    public Flux<Customer> query(){
        return queryCustomerUseCase.query();
    }
}
