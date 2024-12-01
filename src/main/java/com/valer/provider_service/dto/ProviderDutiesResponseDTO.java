package com.valer.provider_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.valer.provider_service.models.ProviderDuty;

import io.swagger.v3.oas.annotations.media.Schema;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель списка услуг")
public class ProviderDutiesResponseDTO {
    
    @Schema(description = "Номер заявки")
    private Integer connectionRequestId;

    @Schema(description = "Количество услуг, добавленных в заявку на подключение")
    private Integer itemsInCart;
    
    @Schema(description = "Список услуг провайдера")
    private List<ProviderDuty> providerServiceList;
}
