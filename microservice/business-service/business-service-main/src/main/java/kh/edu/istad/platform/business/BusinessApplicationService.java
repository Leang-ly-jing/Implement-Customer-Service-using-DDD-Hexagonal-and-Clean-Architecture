package kh.edu.istad.platform.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication(scanBasePackages = "kh.edu.istad.platform")
@EnableReactiveMongoRepositories(basePackages = "kh.edu.istad.platform.persistence.repository")
public class BusinessApplicationService {

    static void main(String[] args) {
        SpringApplication.run(BusinessApplicationService.class, args);
    }
}
