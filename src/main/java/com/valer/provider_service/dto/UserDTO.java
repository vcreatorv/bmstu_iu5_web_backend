package com.valer.provider_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель пользователя")
public class UserDTO {
    @Schema(description = "Логин пользователя", example = "user123")
    private String login;

    @Schema(description = "Пароль пользователя", example = "password123")
    private String password;

    @Schema(description = "Имя пользователя", example = "John Doe")
    private String username;

    @Schema(description = "Роль пользователя", example = "BUYER")
    private String role; 
}