package kh.edu.istad.platform.order.client;

import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
public class CustomerWebClientAdapter implements CustomerRepository {

    private final WebClient webClient;

    public CustomerWebClientAdapter(
            WebClient.Builder webClientBuilder,
            @Value("${services.customer.url:http://customer-service}") String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public boolean exists(UUID customerId) {
        try {
            return Boolean.TRUE.equals(
                    webClient.get()
                            .uri("/api/customers/{id}", customerId) // Calls specific customer
                            .retrieve()
                            .toBodilessEntity()
                            .map(res -> res.getStatusCode().is2xxSuccessful())
                            .onErrorReturn(false)
                            .block()
            );
        } catch (Exception ex) {
            return false;
        }
    }
}
