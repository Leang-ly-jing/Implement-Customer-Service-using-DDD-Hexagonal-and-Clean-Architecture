package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.platform.order.domain.entity.Business;
import kh.edu.istad.platform.order.domain.port.out.BusinessRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class BusinessRepositoryAdapter implements BusinessRepository {
    @Override
    public Optional<Business> findBusinessInformation(Business business) {
        return Optional.of(Business.Builder.builder()
                        .businessId(business.getId())
                        .products(business.getProducts())
                        .active(true)
                .build());
    }
}
