package com.valer.provider_service.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Entity
@Table(name = "connection_requests")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель запроса на подключение")
public class ConnectionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор запроса", example = "1")
    private int id;

    @Column(length = 10, nullable = false)
    @Schema(description = "Статус запроса", example = "DRAFT")
    private String status = "DRAFT";

    @Column(length = 50)
    @Schema(description = "Имя потребителя", example = "Иван Иванов")
    private String consumer;

    @Column(name = "phone_number", length = 18)
    @Schema(description = "Номер телефона", example = "+7 (999) 123-45-67")
    private String phoneNumber;

    @Column(name = "creation_datetime", nullable = false)
    @CreationTimestamp
    @Schema(description = "Дата и время создания")
    private LocalDateTime creationDatetime;

    @Column(name = "formation_datetime")
    @Schema(description = "Дата и время формирования")
    private LocalDateTime formationDatetime;

    @Column(name = "completion_datetime")
    @Schema(description = "Дата и время завершения")
    private LocalDateTime completionDatetime;

    @Column(name = "total_price")
    @Schema(description = "Общая стоимость", example = "1000")
    private Integer totalPrice;

    @ManyToOne
    @JoinColumn(name = "manager")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(description = "Менеджер")
    private User manager;

    @ManyToOne
    @JoinColumn(name = "client", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(description = "Клиент")
    private User client;

    public ConnectionRequest(User client) {
        this.client = client;
    }
}