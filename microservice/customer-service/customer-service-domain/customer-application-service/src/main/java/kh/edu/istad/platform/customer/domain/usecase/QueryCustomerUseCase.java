package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatistic;
import kh.edu.istad.platform.customer.domain.dto.Filtering;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QueryCustomerUseCase {
    private final CustomerRepository customerRepository;
    public Flux<Customer> findAll(){
        return customerRepository.findAll();
    }

    public Mono<Customer> findById(CustomerId id){
        return customerRepository.findById(id);
    }

    public Mono<Page<Customer>> findWithFilter(Filtering filtering, Pageable pageable) {
        return customerRepository.findWithFilter(filtering, pageable);
    }

    public Flux<Customer> query(){
        return customerRepository.query();
    }

    public Flux<CustomerStatistic> getStatistics() {
        return customerRepository.getStatistics();
    }
}