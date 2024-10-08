package com.valer.rip.lab1.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.valer.rip.lab1.models.User;
import com.valer.rip.lab1.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(int userID) {
        return userRepository.findById(userID);
    }

    public String authenticateUser(String login, String password) {
        return "Пользователь аутентифицирован";
    }

    public String logoutUser() {
        return "Пользователь деавторизован";
    }

    public int getUserID() {
        return 1;
    }
}