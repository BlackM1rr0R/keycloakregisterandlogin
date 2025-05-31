package com.example.keycloackspringboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {
    private final HelloService helloService;
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }
    @GetMapping("/public")
    public String publicEndpoint() {
        return "Bu endpoint HERKESE açık.";
    }
    @GetMapping("/greet")
    public String greetEndpoint(@RequestParam String name) {
        return helloService.greet(name);
    }
    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Sadece 'admin' rolüne sahip kullanıcılar görebilir.";
    }
}
