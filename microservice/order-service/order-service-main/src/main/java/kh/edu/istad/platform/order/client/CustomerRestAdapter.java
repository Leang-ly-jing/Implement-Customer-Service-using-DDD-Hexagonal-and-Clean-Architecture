package kh.edu.istad.platform.order.client;

import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class CustomerRestAdapter implements CustomerRepository {

    private final RestClient restClient;

    public CustomerRestAdapter(
            @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder builder,
            @Value("${services.customer.url}") String baseUrl
    ) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    @Override
    public boolean exists(UUID customerId) {
        try {
            return restClient.get()
                    .uri("/api/customers/{id}", customerId)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {})
                    .toBodilessEntity()
                    .getStatusCode()
                    .is2xxSuccessful();
        } catch (Exception ex) {
            return false;
        }
    }
}
