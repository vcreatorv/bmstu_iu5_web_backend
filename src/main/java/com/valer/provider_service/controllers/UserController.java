package com.valer.provider_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.valer.provider_service.dto.AuthRequestDTO;
import com.valer.provider_service.dto.JwtResponseDTO;
import com.valer.provider_service.dto.UserDTO;
import com.valer.provider_service.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name="Пользователи", description="Позволяет получить информацию о клиентах провайдера")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create")
    @Operation(
        summary = "Регистрация пользователя",
        description = "Позволяет зарегистрировать нового пользователя"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
                     content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> createUser(@RequestBody UserDTO userRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(userRequest));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    @Operation(
        summary = "Изменение данных пользователя",
        description = "Позволяет изменить данные ЛК пользователя"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Данные пользователя успешно обновлены",
                     content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userRequest));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(
        summary = "Аутентификация пользователя",
        description = "Позволяет пользователю залогиниться"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь успешно аутентифицирован",
                     content = @Content(schema = @Schema(implementation = JwtResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Неверные учетные данные",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<JwtResponseDTO> loginUser(@RequestBody AuthRequestDTO authRequestDTO){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(authRequestDTO));
        } 
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/logout")
    @Operation(
        summary = "Деавторизация пользователя",
        description = "Позволяет пользователю разлогиниться"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь успешно разлогинен",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Ошибка при выходе из системы",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.logoutUser(request));
    }

}