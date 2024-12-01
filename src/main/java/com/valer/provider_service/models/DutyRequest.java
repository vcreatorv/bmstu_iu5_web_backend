package com.valer.provider_service.models;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Entity
@Table(name = "duties_requests", uniqueConstraints = {@UniqueConstraint(columnNames = {"provider_duty", "connection_request"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель запроса на услугу")
public class DutyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор запроса на услугу", example = "1")
    private int id;

    @ManyToOne
    @JoinColumn(name = "provider_duty", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(description = "Услуга провайдера")
    private ProviderDuty providerDuty;

    @ManyToOne
    @JoinColumn(name = "connection_request", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(description = "Запрос на подключение")
    private ConnectionRequest connectionRequest;

    @Column(nullable = false, columnDefinition = "int default 1")
    @Schema(description = "Количество", example = "1")
    private int amount = 1;
}