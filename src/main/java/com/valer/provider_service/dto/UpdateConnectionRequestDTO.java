package com.valer.provider_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель для обновления запроса на подключение")
public class UpdateConnectionRequestDTO {
    @NotNull
    @Schema(description = "Имя потребителя", example = "Иван Иванов")
    private String consumer;

    @NotNull
    @Schema(description = "Номер телефона", example = "+7 (999) 123-45-67")
    private String phoneNumber;
}