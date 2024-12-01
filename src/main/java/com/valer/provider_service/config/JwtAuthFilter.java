package com.valer.provider_service.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.valer.provider_service.helpers.UserDetailsServiceImpl;
import com.valer.provider_service.models.User;
import com.valer.provider_service.repositories.UserRepository;
import com.valer.provider_service.services.JwtService;
import com.valer.provider_service.services.TokenBlacklistService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenBlacklistService tokenBlacklistService;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException 
    {
        try 
        {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String login = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) 
            {
                token = authHeader.substring(7);
                login = jwtService.extractUsername(token);
            }

            if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) 
            {
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(login);
                User user = userRepository.findByLogin(login).orElseThrow(() -> new IOException("Пользователь не найден!"));
                
                if (jwtService.validateToken(token, userDetails) && !tokenBlacklistService.isBlacklisted(token, user.getId())) 
                {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        } 
        catch (ExpiredJwtException e) 
        {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"JWT токен истек. Пожалуйста, выполните повторную авторизацию.\"}");
        }
    }
}