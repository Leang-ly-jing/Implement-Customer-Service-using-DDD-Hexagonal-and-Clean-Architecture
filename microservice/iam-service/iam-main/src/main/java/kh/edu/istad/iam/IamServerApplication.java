package kh.edu.istad.iam;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;


@SpringBootApplication
@Controller
public class IamServerApplication {
//
//    @GetMapping("/login")
//    public String login(Model model){
//        return "login";
//    }

    public static void main(String[] args) {
        SpringApplication.run(IamServerApplication.class, args);
    }

}