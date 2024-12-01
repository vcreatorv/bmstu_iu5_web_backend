package com.valer.provider_service.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.valer.provider_service.dto.JwtResponseDTO;
import com.valer.provider_service.helpers.CustomUserDetails;
import com.valer.provider_service.helpers.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;


@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String login = extractUsername(token);
        return (login.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public JwtResponseDTO GenerateToken(String login) {
        Map<String, Object> claims = new HashMap<>();
        String accessToken = createToken(claims, login);
        long expiresIn = 1000 * 60 * 20; // 20 minutes in milliseconds
        return new JwtResponseDTO(accessToken, expiresIn);
    }

    private String createToken(Map<String, Object> claims, String login) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(login);
        claims.put("role", ((CustomUserDetails) userDetails).getAuthorities().iterator().next().getAuthority());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(login)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 20)) // Jwt Token is valid for 20 mins
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        // Get the Authorization header from the request
        String authorizationHeader = request.getHeader("Authorization");

        // Check if the Authorization header is not null and starts with "Bearer "
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            // Extract the JWT token (remove "Bearer " prefix)
            return authorizationHeader.substring(7);
        }

        // If the Authorization header is not valid, return null
        return null;
    }
}

// @Component
// public class JwtService {

//     @Value("${jwt.secret}")
//     private String SECRET_KEY;

//     public String extractUsername(String token) {
//         return extractClaim(token, Claims::getSubject);
//     }

//     public Date extractExpiration(String token) {
//         return extractClaim(token, Claims::getExpiration);
//     }

//     public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//         final Claims claims = extractAllClaims(token);
//         return claimsResolver.apply(claims);
//     }

//     private Claims extractAllClaims(String token) {
//         return Jwts
//                 .parserBuilder()
//                 .setSigningKey(getSignKey())
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody();
//     }

//     private Boolean isTokenExpired(String token) {
//         return extractExpiration(token).before(new Date());
//     }

//     public Boolean validateToken(String token, UserDetails userDetails) {
//         final String login = extractUsername(token);
//         return (login.equals(userDetails.getUsername()) && !isTokenExpired(token));
//     }

//     public String GenerateToken(String login){
//         Map<String, Object> claims = new HashMap<>();
//         return createToken(claims, login);
//     }

//     private String createToken(Map<String, Object> claims, String login) {

//         return Jwts.builder()
//                 .setClaims(claims)
//                 .setSubject(login)
//                 .setIssuedAt(new Date(System.currentTimeMillis()))
//                 .setExpiration(new Date(System.currentTimeMillis()+1000*60*20)) // Jwt Token is valid for 20 mins
//                 .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
//     }

//     private Key getSignKey() {
//         byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//         return Keys.hmacShaKeyFor(keyBytes);
//     }

//     public String extractTokenFromRequest(HttpServletRequest request) {
//         // Get the Authorization header from the request
//         String authorizationHeader = request.getHeader("Authorization");

//         // Check if the Authorization header is not null and starts with "Bearer "
//         if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
//             // Extract the JWT token (remove "Bearer " prefix)
//             return authorizationHeader.substring(7);
//         }

//         // If the Authorization header is not valid, return null
//         return null;
//     }
// }