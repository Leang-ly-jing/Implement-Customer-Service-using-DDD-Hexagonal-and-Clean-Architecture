package kh.edu.istad.platform.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication(scanBasePackages = "kh.edu.istad.platform")
@EnableReactiveMongoRepositories(basePackages = "kh.edu.istad.platform")
public class PaymentApplicationService {

    static void main(String[] args) {
        SpringApplication.run(PaymentApplicationService.class, args);
    }
}
