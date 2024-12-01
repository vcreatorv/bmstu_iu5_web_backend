package com.valer.provider_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;



@Entity
@Table(name = "provider_duties")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель услуги провайдера")
public class ProviderDuty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор услуги", example = "1")
    private int id;

    @Column(unique = true, length = 255, nullable = false)
    @Schema(description = "Название услуги", example = "Интернет 100 Мбит/с")
    private String title;

    @Column(name = "img_url", length = 255)
    @Schema(description = "URL изображения услуги", example = "http://example.com/image.jpg")
    private String imgUrl;

    @Column(columnDefinition = "text", nullable = false)
    @Schema(description = "Описание услуги", example = "Высокоскоростной интернет для дома")
    private String description;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    @Schema(description = "Флаг активности услуги", example = "true")
    private Boolean active = true;

    @Column(nullable = false, columnDefinition = "int default 0")
    @Schema(description = "Цена услуги", example = "500")
    private int price = 0;

    @Column(name = "monthly_payment", nullable = false, columnDefinition = "boolean default false")
    @Schema(description = "Флаг ежемесячной оплаты", example = "true")
    private Boolean monthlyPayment = false;

    @Column(length = 10, nullable = false)
    @Schema(description = "Единица измерения", example = "Мбит/с")
    private String unit;

    @Column(name = "amount_description", length = 100, nullable = false)
    @Schema(description = "Описание количества", example = "100 Мбит/с")
    private String amountDescription;

    public Boolean getMonthlyPayment() {
        return monthlyPayment;
    }

    public Boolean getActive() {
        return active;
    }
}