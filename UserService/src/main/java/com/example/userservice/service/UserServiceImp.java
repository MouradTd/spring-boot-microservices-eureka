package com.example.userservice.service;

import com.example.userservice.config.KeycloakConfig;
import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;
import org.keycloak.admin.client.resource.UsersResource;
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
    public String assignRealmRoleToUser(String userId, String roleName) {
        try {
            UserRepresentation user = keycloakConfig.getUsersResource().get(userId).toRepresentation();
            RoleRepresentation role = keycloakConfig.getRealmResource().roles().get(roleName).toRepresentation();

            keycloakConfig.getUsersResource().get(userId).roles().realmLevel().add(Collections.singletonList(role));
            return "Role assigned successfully";
        } catch (NotFoundException e) {
            return "User or role not found: " + e.getMessage();
        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
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
