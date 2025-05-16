package com.example.userservice.controller;

import com.example.userservice.dto.PasswordDto;
import com.example.userservice.dto.RoleDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;
import com.example.userservice.service.UserServiceImp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImp userService;
    public UserController(UserServiceImp userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDto user) {
        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data");
        }
        String result = userService.createUser(user);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/assign-role/{id}")
    public ResponseEntity<String> assignRealmRoleToUser(@PathVariable String id, @RequestBody RoleDto roleDto) {
        if (id == null || roleDto.getRoleName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data");
        }
        String result = userService.assignRealmRoleToUser(id, roleDto.getRoleName());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/reset-password/{id}")
    public ResponseEntity<String> resetPassword(@PathVariable String id, @RequestBody PasswordDto passwordDto) {
        if (id == null || passwordDto.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data");
        }
        String result = userService.resetPassword(id, passwordDto.getPassword());
        return ResponseEntity.ok(result);
    }
}
