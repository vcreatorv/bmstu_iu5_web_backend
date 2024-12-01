package com.valer.provider_service.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.valer.provider_service.dto.AuthRequestDTO;
import com.valer.provider_service.dto.JwtResponseDTO;
import com.valer.provider_service.dto.UserDTO;
import com.valer.provider_service.helpers.Role;
import com.valer.provider_service.models.ConnectionRequest;
import com.valer.provider_service.models.User;
import com.valer.provider_service.repositories.ConnectionRequestRepository;
import com.valer.provider_service.repositories.UserRepository;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConnectionRequestRepository connectionRequestRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    TokenBlacklistService tokenBlacklistService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    ModelMapper modelMapper;

    @PostConstruct
    public void setupMapper() 
    {
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(UserDTO.class, User.class)
            .addMappings(mapper -> mapper.map(src -> src.getRole() != null ? Role.valueOf(src.getRole()) : Role.BUYER, User::setRole));

        modelMapper.typeMap(User.class, UserDTO.class)
            .addMappings(mapper -> mapper.map(User::getRole, (dest, v) -> 
            {
                if (v == null) 
                {
                    dest.setRole(Role.BUYER.name());
                } 
                else 
                {
                    dest.setRole(((Role) v).name());
                }
            }));
    }

    public UserDTO createUser(UserDTO userRequest) throws Exception 
    {
        if(userRequest.getLogin() == null || userRequest.getPassword() == null)
        {
            throw new Exception("Параметры login и password не могут быть пустыми!");
        }

        User user = modelMapper.map(userRequest, User.class);

        user.setPassword(encodePassword(userRequest.getPassword()));

        userRepository.save(user);
        UserDTO userResponse = modelMapper.map(user, UserDTO.class);
        return userResponse;
    }

    public UserDTO updateUser(UserDTO userRequest) throws Exception 
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String login = userDetail.getUsername();
        
        User currentUser = userRepository.findByLogin(login).orElseThrow(() -> new Exception("Пользователь не найден!"));
        
        // Обновляем только непустые поля
        if (userRequest.getUsername() != null && !userRequest.getUsername().isEmpty()) 
        {
            currentUser.setUsername(userRequest.getUsername());
        }
        
        if (userRequest.getLogin() != null && !userRequest.getLogin().isEmpty()) 
        {
            currentUser.setLogin(userRequest.getLogin());
        }
        
        // Обновляем пароль только если он предоставлен
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) 
        {
            currentUser.setPassword(encodePassword(userRequest.getPassword()));
        }
        
        userRepository.save(currentUser);
        
        return modelMapper.map(currentUser, UserDTO.class);
    }

    public JwtResponseDTO loginUser(AuthRequestDTO authRequestDTO) 
    {
        try 
        {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getLogin(), authRequestDTO.getPassword())
            );
            
            if (authentication.isAuthenticated()) 
            {
                return jwtService.GenerateToken(authRequestDTO.getLogin());
            } 
            else 
            {
                throw new UsernameNotFoundException("Неверные учетные данные");
            }
        } 
        catch (AuthenticationException e) 
        {
            throw new UsernameNotFoundException("Неверные учетные данные", e);
        }
    }

    public String logoutUser(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String login = userDetail.getUsername();
        int userId = userRepository.findByLogin(login).get().getId();
        tokenBlacklistService.addToBlacklist(request, userId);
        return "Вы успешно разлогинились!";
    }

    public boolean isOwnerOfRequest(int requestId, String login) {
        ConnectionRequest request = connectionRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Заявка " + requestId + " пользователя " + login + " не найдена!"));
        return request.getClient().getLogin().equals(login);
    }

    public String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}