package com.example.keycloackspringboot;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final KeycloakService keycloakService;
    public AuthController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }
    @PostMapping("/register")
    public String registerUser(@RequestBody RegisterRequest request) {
        return keycloakService.registerUser(request);
    }
}
