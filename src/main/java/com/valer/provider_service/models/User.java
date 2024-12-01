package com.valer.provider_service.models;

import com.valer.provider_service.helpers.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;


@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@Schema(description = "Модель пользователя")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор пользователя", example = "1")
    private int id;

    @Column(length = 255, nullable = false, unique = true)
    @Schema(description = "Логин пользователя", example = "user123")
    private String login;

    @Column(length = 150, nullable = false)
    @Schema(description = "Пароль пользователя", example = "password123")
    private String password;

    @Column(nullable = false)
    @Schema(description = "Роль пользователя (числовое представление)", example = "1")
    private int role;

    @Column(nullable = false)
    @Schema(description = "Имя пользователя", example = "John Doe")
    private String username;

    public User() {
        this.role = Role.BUYER.getValue();
    }

    @Schema(description = "Получение роли пользователя")
    public Role getRole() {
        return Role.fromValue(role);
    }

    @Schema(description = "Установка роли пользователя")
    public void setRole(Role role) {
        this.role = role.getValue();
    }
}