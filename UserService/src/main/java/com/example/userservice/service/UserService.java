package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface UserService {
    String createUser(UserDto userData);
    String assignRealmRoleToUser(String userId, String roleName);
    String resetPassword(String userId, String newPassword);
    void deleteUser(String userId);
    List<UserRepresentation> findUsersByUsername(String username);
}
