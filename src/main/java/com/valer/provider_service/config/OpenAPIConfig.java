package com.valer.provider_service.config;

import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "NET4Y API",
                description = "REST API провайдера для составления заявок на подключение и просмотра услуг.\n\n Полезные ссылки:\n<ul><li>https://github.com/vcreatorv/bmstu_iu5_web</li></ul>", version = "1.0.0",
                contact = @Contact(
                        name = "Валерий Нагапетян",
                        email = "valery.nagapetyan@yandex.ru",
                        url = "https://vk.com/yep_idk"
                )
        )
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPIConfig 
{
        @Bean
        public OpenAPI customOpenAPI() 
        {
        return new OpenAPI()
                .components(new Components());
        }
}