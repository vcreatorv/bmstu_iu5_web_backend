package com.valer.provider_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Модель услуги провайдера в запросе")
public class ProviderDutyInRequestDTO 
{
    @Schema(description = "Идентификатор услуги", example = "1")
    private int id;

    @Schema(description = "Название услуги", example = "Интернет 100 Мбит/с")
    private String title;

    @Schema(description = "Цена услуги", example = "500")
    private int price;

    @Schema(description = "Флаг ежемесячной оплаты", example = "true")
    private Boolean monthlyPayment;

    @Schema(description = "Единица измерения", example = "Мбит/с")
    private String unit;

    @Schema(description = "Описание количества", example = "100 Мбит/с")
    private String amountDescription;

    @Schema(description = "URL изображения услуги", example = "http://example.com/image.jpg")
    private String imgUrl;

    @Schema(description = "Количество", example = "1")
    private int amount;
}