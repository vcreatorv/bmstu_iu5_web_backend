package com.valer.provider_service.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель запроса аутентификации")
public class AuthRequestDTO 
{
    @Schema(description = "Логин пользователя", example = "user123")
    private String login;

    @Schema(description = "Пароль пользователя", example = "password123")
    private String password;
}
