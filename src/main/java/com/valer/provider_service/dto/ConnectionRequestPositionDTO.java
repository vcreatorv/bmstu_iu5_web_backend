package com.valer.provider_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель позиции в запросе на подключение")
public class ConnectionRequestPositionDTO 
{
    @Schema(description = "Идентификатор позиции", example = "1")
    private int id;

    @Schema(description = "Услуга провайдера")
    private ProviderDutyInRequestDTO providerDuty;

    @Schema(description = "Запрос на подключение")
    private ConnectionRequestDTO connectionRequest;
}