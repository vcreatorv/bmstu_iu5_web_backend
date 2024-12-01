package com.valer.provider_service.dto;

import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Модель запроса на подключение")
public class ConnectionRequestDTO {
    @Schema(description = "Идентификатор запроса", example = "1")
    private int id;

    @Schema(description = "Статус запроса", example = "DRAFT")
    private String status;

    @Schema(description = "Имя потребителя", example = "Иван Иванов")
    private String consumer;

    @Schema(description = "Номер телефона", example = "+7 (999) 123-45-67")
    private String phoneNumber;

    @Schema(description = "Дата и время создания")
    private LocalDateTime creationDatetime;

    @Schema(description = "Дата и время формирования")
    private LocalDateTime formationDatetime;

    @Schema(description = "Дата и время завершения")
    private LocalDateTime completionDatetime;

    @Schema(description = "Общая стоимость", example = "1000")
    private Integer totalPrice;

    @Schema(description = "Менеджер", example = "manager1")
    private String manager; 

    @Schema(description = "Клиент", example = "client1")
    private String client;

    @Schema(description = "Список услуг в запросе")
    private List<ProviderDutyInRequestDTO> providerServiceList;
}