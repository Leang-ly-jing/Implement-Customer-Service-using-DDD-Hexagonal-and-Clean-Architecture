package kh.edu.istad.platform.order.adapter;

import kh.edu.istad.platform.customer.proto.CustomerGrpcServiceGrpc;
import kh.edu.istad.platform.customer.proto.CustomerRequest;
import kh.edu.istad.platform.customer.proto.CustomerResponse;
import kh.edu.istad.platform.order.domain.dto.QueryCustomerResult;
import kh.edu.istad.platform.order.domain.port.out.client.CustomerServiceClient;
import kh.edu.istad.platform.order.httpclient.CustomerHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
@Component
@RequiredArgsConstructor
public class CustomerServiceClientAdapter implements CustomerServiceClient {

    private final CustomerHttpClient customerHttpClient;
    private final CustomerGrpcServiceGrpc.CustomerGrpcServiceBlockingStub customerGrpcStub;

    @Override
    public Flux<QueryCustomerResult> getCustomers() {
        return customerHttpClient.queryCustomers();
    }


    public Mono<QueryCustomerResult> getCustomerByIdViaGrpc(String customerId) {
        return Mono.fromCallable(() -> {
            CustomerRequest request = CustomerRequest.newBuilder()
                    .setCustomerId(customerId)
                    .build();

            CustomerResponse response = customerGrpcStub.getCustomerById(request);
            return new QueryCustomerResult(response.getFamilyName(), response.getGivenName());
        }).subscribeOn(Schedulers.boundedElastic());
    }
}