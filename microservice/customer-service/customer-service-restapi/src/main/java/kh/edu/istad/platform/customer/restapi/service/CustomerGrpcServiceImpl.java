package kh.edu.istad.platform.customer.restapi.service;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.usecase.QueryCustomerUseCase;
import kh.edu.istad.platform.customer.proto.CustomerGrpcServiceGrpc;
import kh.edu.istad.platform.customer.proto.CustomerRequest;
import kh.edu.istad.platform.customer.proto.CustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class CustomerGrpcServiceImpl extends CustomerGrpcServiceGrpc.CustomerGrpcServiceImplBase {

    private final QueryCustomerUseCase queryCustomerUseCase;

    @Override
    public void getCustomerById(CustomerRequest request, StreamObserver<CustomerResponse> responseObserver) {
        UUID customerUuid;
        try {
            customerUuid = UUID.fromString(request.getCustomerId());
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid UUID format: " + request.getCustomerId())
                            .asRuntimeException()
            );
            return;
        }

        queryCustomerUseCase.findById(new CustomerId(customerUuid))
                .switchIfEmpty(Mono.error(
                        Status.NOT_FOUND
                                .withDescription("Customer not found with id: " + request.getCustomerId())
                                .asRuntimeException()
                ))
                .subscribe(
                        customer -> {
                            CustomerResponse response = CustomerResponse.newBuilder()
                                    .setCustomerId(customer.getId().value().toString())
                                    .setFamilyName(customer.getFamilyName() != null ? customer.getFamilyName() : "")
                                    .setGivenName(customer.getGivenName() != null ? customer.getGivenName() : "")
                                    .setEmail(customer.getEmail() != null ? customer.getEmail().value() : "")
                                    .setStatus(customer.getCustomerStatus() != null ? customer.getCustomerStatus().name() : "")
                                    .build();

                            log.info("Found customer from MongoDB via gRPC: {}", response.getCustomerId());
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        error -> {
                            log.error("Error retrieving customer via gRPC: {}", error.getMessage());
                            if (error instanceof StatusRuntimeException statusException) {
                                responseObserver.onError(statusException);
                            } else {
                                responseObserver.onError(
                                        Status.INTERNAL
                                                .withDescription(error.getMessage())
                                                .asRuntimeException()
                                );
                            }
                        }
                );
    }
}