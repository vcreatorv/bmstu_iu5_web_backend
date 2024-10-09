package com.valer.rip.lab1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valer.rip.lab1.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.valer.rip.lab1.dto.UserDTO;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<? extends Object> createUser(@ModelAttribute UserDTO userDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(userDTO));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<? extends Object> authenticateUser(@ModelAttribute UserDTO userDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.authenticateUser(userDTO));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<? extends Object> logoutUser(@ModelAttribute UserDTO userDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.logoutUser(userDTO));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{userID}/update")
    public ResponseEntity<? extends Object> updateUser(@PathVariable("userID") int userID, @ModelAttribute UserDTO userDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userID, userDTO));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
