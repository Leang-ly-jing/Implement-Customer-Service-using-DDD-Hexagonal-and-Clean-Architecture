package kh.edu.istad.platform.order;


import kh.edu.istad.platform.order.adapter.CustomerServiceClientAdapter;
import kh.edu.istad.platform.order.domain.dto.QueryCustomerResult;
import kh.edu.istad.platform.order.domain.port.out.client.CustomerServiceClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController

public class WebFluxController {

    private final CustomerServiceClient customerServiceClient;
    private final CustomerServiceClientAdapter customerServiceClientAdapter;


    @GetMapping("/customers/grpc/{customerId}")
    public Mono<QueryCustomerResult> getCustomerViaGrpc(@PathVariable String customerId) {
        return customerServiceClientAdapter.getCustomerByIdViaGrpc(customerId);
    }

    public WebFluxController(CustomerServiceClient customerServiceClient,  CustomerServiceClientAdapter customerServiceClientAdapter) {
        this.customerServiceClient = customerServiceClient;
        this.customerServiceClientAdapter = customerServiceClientAdapter;
    }

    @GetMapping("customers")
    public Flux<QueryCustomerResult> getCustomers(){
        return customerServiceClient.getCustomers();
    }


    @GetMapping("/flux")
    public Flux<String> getFluxData() {
        return Flux.just("A", "B", "C", "D", "E", "F")
                .log()
                .delayElements(Duration.ofSeconds(1));
    }

}
