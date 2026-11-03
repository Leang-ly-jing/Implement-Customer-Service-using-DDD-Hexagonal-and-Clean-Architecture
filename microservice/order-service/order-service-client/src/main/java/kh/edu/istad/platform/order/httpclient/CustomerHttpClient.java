package kh.edu.istad.platform.order.httpclient;

import kh.edu.istad.platform.order.domain.dto.QueryCustomerResult;
import org.springframework.security.oauth2.client.annotation.ClientRegistrationId;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Flux;

@HttpExchange("api/customers")
@ClientRegistrationId("customer-client")
public interface CustomerHttpClient {
    @GetExchange
    Flux<QueryCustomerResult> queryCustomers();

}
