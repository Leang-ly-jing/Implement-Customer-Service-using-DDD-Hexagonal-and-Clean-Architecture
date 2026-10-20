package kh.edu.istad.platform.customer.persistence.filter;

import kh.edu.istad.platform.customer.domain.dto.Filtering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.*;

public interface FilterableRepository<T> {

    default Query constructQueryFromFiltering(Filtering filtering) {
        Query query = new Query();
        if (filtering == null || filtering.getFilterList().isEmpty()) {
            return query;
        }

        Map<String, Criteria> criteriaMap = new HashMap<>();
        for (Filtering.Filter filter : filtering.getFilterList()) {
            switch (filter.getOperator()) {
                case eq -> criteriaMap.put(filter.getKey(), Criteria.where(filter.getKey()).is(filter.getValue()));
                case gt -> criteriaMap.computeIfAbsent(filter.getKey(), Criteria::where).gt(filter.getValue());
                case gte -> criteriaMap.computeIfAbsent(filter.getKey(), Criteria::where).gte(filter.getValue());
                case lt -> criteriaMap.computeIfAbsent(filter.getKey(), Criteria::where).lt(filter.getValue());
                case lte -> criteriaMap.computeIfAbsent(filter.getKey(), Criteria::where).lte(filter.getValue());
                case ne -> criteriaMap.put(filter.getKey(), Criteria.where(filter.getKey()).ne(filter.getValue()));
                case in -> criteriaMap.put(filter.getKey(), Criteria.where(filter.getKey()).in((Collection<?>) filter.getValue()));
                case nin -> criteriaMap.put(filter.getKey(), Criteria.where(filter.getKey()).nin((Collection<?>) filter.getValue()));
                case regex -> criteriaMap.put(filter.getKey(), Criteria.where(filter.getKey()).regex((String) filter.getValue()));
                default -> throw new IllegalArgumentException("Unknown operator: " + filter.getOperator());
            }
        }
        criteriaMap.values().forEach(query::addCriteria);
        return query;
    }

    // Reactive pagination using Mono.zip
    default Mono<Page<T>> findAllWithFilter(ReactiveMongoTemplate template, Class<T> clazz, Filtering filtering, Pageable pageable) {
        Query query = constructQueryFromFiltering(filtering);

        Mono<List<T>> listMono = template.find(Query.of(query).with(pageable), clazz).collectList();
        Mono<Long> countMono = template.count(query, clazz);

        return Mono.zip(listMono, countMono)
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }
}
