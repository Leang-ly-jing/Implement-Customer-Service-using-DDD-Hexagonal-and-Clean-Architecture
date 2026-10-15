package kh.edu.istad.platform.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = "kh.edu.istad.platform.persistence.repository")
public class BusinessApplication {

    static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }
}
