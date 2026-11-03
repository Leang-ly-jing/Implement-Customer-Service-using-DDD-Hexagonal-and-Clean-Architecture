
package kh.edu.istad.platform.order.adapter;

import kh.edu.istad.platform.order.domain.dto.QueryCustomerResult;
import kh.edu.istad.platform.order.domain.port.out.client.CustomerServiceClient;
import kh.edu.istad.platform.order.httpclient.CustomerHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class CustomerServiceClientAdapter implements CustomerServiceClient {

    private final CustomerHttpClient customerHttpClient;

    @Override
    public Flux<QueryCustomerResult> getCustomers() {

        return customerHttpClient.queryCustomers();
    }
}
