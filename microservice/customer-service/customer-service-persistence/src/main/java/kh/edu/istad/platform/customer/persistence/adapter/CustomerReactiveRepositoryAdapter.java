package kh.edu.istad.platform.customer.persistence.adapter;


import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistence.filter.FilterableRepository;
import kh.edu.istad.platform.customer.domain.dto.Filtering;
import kh.edu.istad.platform.customer.persistence.mapper.CustomerPersistenceMapper;
import kh.edu.istad.platform.customer.persistence.repository.CustomerMongoRepository;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatistic;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Component
@RequiredArgsConstructor
public class CustomerReactiveRepositoryAdapter implements CustomerRepository, FilterableRepository<CustomerDocument> {
    private final CustomerMongoRepository customerMongoRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    @Override
    public Mono<Customer> save(Customer customer) {
        CustomerDocument customerDocument = customerPersistenceMapper.toDocument(customer);
        Mono<CustomerDocument> result = reactiveMongoTemplate.save(customerDocument);

        return result.map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Flux<Customer> findAll() {
        return customerMongoRepository.findAll().map(
                customerPersistenceMapper::toDomain
        );
    }

    @Override
    public Mono<Customer> findById(CustomerId id) {
        return customerMongoRepository.findById(id.value().toString()).map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(CustomerId id) {
        return customerMongoRepository.deleteById(id.value().toString());
    }

    @Override
    public Mono<Customer> findByUsername(String userName) {
        return customerMongoRepository.findByUsername(userName).map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Flux<Customer> query() {
        Query query = Query.query(
                Criteria.where("userName").is("Jing jin")
        );
        return reactiveMongoTemplate.find(query, CustomerDocument.class).map(customerPersistenceMapper::toDomain);

    }


    @Override
    public Mono<Page<Customer>> findWithFilter(Filtering filtering, Pageable pageable) {
        return findAllWithFilter(reactiveMongoTemplate, CustomerDocument.class, filtering, pageable)
                .map(page -> page.map(customerPersistenceMapper::toDomain));
    }

    @Override
    public Flux<CustomerStatistic> getStatistics() {
        GroupOperation group = Aggregation.group("customerStatus", "segment").count().as("total");
        ProjectionOperation project = Aggregation.project("total")
                .and("_id.customerStatus").as("status")
                .and("_id.segment").as("segmentId");

        Aggregation aggregation = Aggregation.newAggregation(group, project);
        return reactiveMongoTemplate.aggregate(aggregation, CustomerDocument.class, CustomerStatistic.class);
    }
}
