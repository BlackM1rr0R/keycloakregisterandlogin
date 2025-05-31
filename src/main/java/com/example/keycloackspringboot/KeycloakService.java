package com.example.keycloackspringboot;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class KeycloakService {

    private final String serverUrl = "http://localhost:8080";
    private final String realm = "myrealm";
    private final String clientId = "admin-cli";
    private final String adminUsername = "admin";
    private final String adminPassword = "admin";

    public String registerUser(RegisterRequest request) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .clientId(clientId)
                .username(adminUsername)
                .password(adminPassword)
                .build();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getName());
        user.setLastName(request.getSurname());
        user.setEnabled(true);

        Response response = keycloak.realm(realm).users().create(user);

        if (response.getStatus() != 201) {
            return "Kayıt başarısız. HTTP kodu: " + response.getStatus();
        }

        URI location = response.getLocation();
        if (location == null) {
            return "Kullanıcı oluşturuldu ama ID alınamadı.";
        }
        String userId = location.getPath().replaceAll(".*/([^/]+)$", "$1");

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());

        keycloak.realm(realm).users().get(userId).resetPassword(credential);

        RoleRepresentation userRole = keycloak.realm(realm)
                .roles()
                .get("user")
                .toRepresentation();

        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(userRole));

        sendVerificationEmailAsync(keycloak, userId);
        return "Kullanıcı başarıyla oluşturuldu ve e-posta doğrulama gönderildi.";
    }

    @Async
    public void sendVerificationEmailAsync(Keycloak keycloak, String userId) {
        try {
            keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .sendVerifyEmail();
        } catch (Exception e) {
            System.err.println("E-posta doğrulama gönderilemedi: " + e.getMessage());
        }
    }
}
