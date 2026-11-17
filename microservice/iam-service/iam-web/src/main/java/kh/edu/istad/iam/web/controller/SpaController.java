package kh.edu.istad.iam.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class SpaController {
    @GetMapping(value = {
            "/login",
            "/register",
            "/account",
            "/account/**",
            "/admin",
            "/admin/**",
            "/management",
            "/management/**"
    })
    public String forwardSpa() {
        return "forward:/index.html";
    }
}