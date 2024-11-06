package com.valer.rip.lab1.services;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.valer.rip.lab1.dto.UserDTO;
import com.valer.rip.lab1.models.User;
import com.valer.rip.lab1.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public User createUser(UserDTO userDTO) throws Exception {
        verifyUserCredentials(userDTO);
        try {
            User user = new User();
            modelMapper.map(userDTO, user);
            return userRepository.save(user);
        }
        catch (Exception e) {
            throw new Exception("Ошибка при создании нового пользователя");
        }
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(int userID) {
        return userRepository.findById(userID);
    }

    public String authenticateUser(UserDTO userDTO) throws Exception {
        verifyUserCredentials(userDTO);
        try {
            userRepository.findByLoginAndPassword(userDTO.getLogin(), userDTO.getPassword())
            .orElseThrow(() -> new Exception("Неверно введен логин или пароль"));
            
            return "Пользователь аутентифицирован";
        }
        catch (Exception e) {
            throw new Exception("Неверно введен логин или пароль");
        }
    }

    public String logoutUser(UserDTO userDTO) throws Exception {
        verifyUserCredentials(userDTO);
        try {
            userRepository.findByLoginAndPassword(userDTO.getLogin(), userDTO.getPassword())
            .orElseThrow(() -> new Exception("Не удалось деавторизовать пользователя"));
            
            return "Пользователь деавторизован";
        }
        catch (Exception e) {
            throw new Exception("Не удалось деавторизовать пользователя");
        }
    }

    public User updateUser(int userID, UserDTO userDTO) throws Exception {
        User user = userRepository.findById(userID)
            .orElseThrow(() -> new Exception("Такого пользователя не существует"));

        if (userDTO.getLogin() != null) {
            user.setLogin(userDTO.getLogin());
        }

        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        
        return user;
    }

    public void verifyUserCredentials(UserDTO userDTO) throws Exception {
        if (userDTO.getLogin() == null || userDTO.getPassword() == null) {
            throw new Exception("Поля логин и пароль должны быть заполнены");
        }
    }

    public int getUserID() {
        return 4;
    }
}