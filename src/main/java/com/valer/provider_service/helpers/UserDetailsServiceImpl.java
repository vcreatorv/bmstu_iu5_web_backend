package com.valer.provider_service.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.valer.provider_service.models.User;
import com.valer.provider_service.repositories.UserRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService 
{

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException 
    {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Не удалось найти пользователя..."));
        logger.info("Пользователь прошел идентификацию!");
        return new CustomUserDetails(user);
    }
}