package kh.edu.istad.platform.order;

import kh.edu.istad.platform.customer.proto.CustomerGrpcServiceGrpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.grpc.client.ImportGrpcClients;

@SpringBootApplication(scanBasePackages = "kh.edu.istad.platform.order")
@ImportGrpcClients(
        target = "customer-service",
        types = CustomerGrpcServiceGrpc.CustomerGrpcServiceBlockingStub.class
)
public class OrderApplicationService {
    static void main(String[] args){
        SpringApplication.run(OrderApplicationService.class, args);
    }
}
