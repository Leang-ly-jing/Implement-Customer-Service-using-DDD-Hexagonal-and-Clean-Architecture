package kh.edu.istad.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class IamServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IamServerApplication.class, args);
    }

}