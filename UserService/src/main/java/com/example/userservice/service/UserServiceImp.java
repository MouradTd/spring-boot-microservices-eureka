package com.example.userservice.service;

import com.example.userservice.config.KeycloakConfig;
import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private KeycloakConfig keycloakConfig;


    @Override
    public String createUser(UserDto userData) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userData.getUsername());
        user.setEmail(userData.getEmail());
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userData.getPassword());

        user.setCredentials(Collections.singletonList(credential));

        Response response = keycloakConfig.getUsersResource().create(user);

        if (response.getStatus() == 201) {
            return "User created successfully.";
        } else {
            return "Failed to create user: " + response.getStatus();
        }
    }



    @Override
    public String assignClientRoleToUser(String userId, String clientId, String roleName) {
        try {
            // Fetch the user from Keycloak
            var userResource = keycloakConfig.getUsersResource().get(userId);
            if (userResource == null) {
                throw new RuntimeException("User not found");
            }
            UserRepresentation user = userResource.toRepresentation();

            // Fetch the client by client ID
            List<ClientRepresentation> clients = keycloakConfig.getRealmResource().clients().findAll();
            ClientRepresentation client = clients.stream()
                    .filter(c -> c.getClientId().equals(clientId))
                    .findFirst()
                    .orElse(null);

            if (client == null) {
                throw new RuntimeException("Client not found for clientId: " + clientId);
            }

            String clientUuid = client.getId();
            System.out.println("Client UUID: " + clientUuid);

            // Fetch the client role
            RoleRepresentation clientRole;
            try {
                clientRole = keycloakConfig.getRealmResource()
                        .clients()
                        .get(clientUuid)
                        .roles()
                        .get(roleName)
                        .toRepresentation();
            } catch (NotFoundException e) {
                throw new RuntimeException( "Role not found for client: " + clientUuid + " Role: " + roleName);
            }

            // Assign the client role to the user
            keycloakConfig.getUsersResource()
                    .get(userId)
                    .roles()
                    .clientLevel(clientUuid)
                    .add(Collections.singletonList(clientRole));

            return "Client role assigned successfully";
        } catch (NotFoundException e) {
            System.err.println("NotFoundException: " + e.getMessage());
            return "User, client, or role not found: " + e.getMessage();
        }
    }

    public String resetPassword(String userId, String newPassword) {
        try {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(newPassword);

            keycloakConfig.getUsersResource()
                    .get(userId)
                    .resetPassword(credential);
            return "Password reset successfully";
        } catch (NotFoundException e) {
            return "User not found: " + e.getMessage();
        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
    }
    public List<UserRepresentation> findUsersByUsername(String username) {
        return keycloakConfig.getUsersResource().search(username, 0, 10);
    }
    public void deleteUser(String userId) {
        keycloakConfig.getUsersResource().get(userId).remove();
    }



}
