
package kh.edu.istad.platform.order.adapter;

import kh.edu.istad.platform.order.domain.port.out.client.CustomerServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class CustomerServiceClientAdapter implements CustomerServiceClient {

    private final WebClient.Builder webClientBuilder;

    public record EmailDto(String value) {}

    public record PhoneNumberDto(String value) {}

    public record GivenName(
            String customerId,
            String userName,
            String familyName,
            String givenName,
            EmailDto email,
            String status,
            PhoneNumberDto phoneNumber
    ) {}

    @Override
    public Flux<CustomerResponse> getCustomerNames() {
        WebClient webClient = webClientBuilder
                .baseUrl("http://customer-service")
                .build();

        return webClient
                .get()
                .uri("/api/customers")
                .retrieve()
                .bodyToFlux(GivenName.class)
                .map(customer -> new CustomerResponse(
                        customer.customerId(),
                        customer.userName(),
                        customer.familyName(),
                        customer.givenName(),
                        customer.email().value(),
                        customer.phoneNumber().value(),
                        customer.status(),
                        null
                ));
    }
}
