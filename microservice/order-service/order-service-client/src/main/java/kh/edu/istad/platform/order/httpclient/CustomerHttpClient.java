package kh.edu.istad.platform.order.httpclient;

import kh.edu.istad.platform.order.domain.dto.QueryCustomerResult;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Flux;

@HttpExchange("api/customers")
public interface CustomerHttpClient {
    @GetExchange
    Flux<QueryCustomerResult> queryCustomers();

}
