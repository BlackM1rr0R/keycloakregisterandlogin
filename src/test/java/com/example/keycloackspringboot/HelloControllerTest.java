package com.example.keycloackspringboot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(HelloController.class)
@Import(HelloControllerTest.HelloServiceTestConfig.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HelloService helloService;

    @TestConfiguration
    static class HelloServiceTestConfig {
        @Bean
        public HelloService helloService() {
            return mock(HelloService.class);
        }
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/public -> herkes erişebilir")
    void testPublicEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/public"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bu endpoint HERKESE açık."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/greet?name=Fatima -> HelloService çağrılır ve yanıt döner")
    void testGreetEndpoint() throws Exception {
        Mockito.when(helloService.greet("Fatima")).thenReturn("Merhaba, Fatima!");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/greet")
                        .param("name", "Fatima"))
                .andExpect(status().isOk())
                .andExpect(content().string("Merhaba, Fatima!"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/admin -> admin rolü gerektirir (dummy test)")
    void testAdminEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Sadece 'admin' rolüne sahip kullanıcılar görebilir."));
    }
}
