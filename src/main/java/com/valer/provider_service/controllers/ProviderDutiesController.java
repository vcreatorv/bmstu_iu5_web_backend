package com.valer.provider_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.valer.provider_service.dto.ConnectionRequestDTO;
import com.valer.provider_service.dto.CreateProviderDutyDTO;
import com.valer.provider_service.dto.ProviderDutiesResponseDTO;
import com.valer.provider_service.dto.UpdateProviderDutyDTO;
import com.valer.provider_service.models.ProviderDuty;
import com.valer.provider_service.services.ProviderDutyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/provider-duties")
@SecurityRequirement(name = "Bearer Authentication")
@Tag
(
    name="Услуги провайдера", 
    description="Позволяет получить информацию об услугах провайдера"
)
public class ProviderDutiesController 
{

    private final ProviderDutyService providerDutyService;

    public ProviderDutiesController(ProviderDutyService providerDutyService)
    {
        this.providerDutyService = providerDutyService;
    }

    @GetMapping
    @Operation
    (
        summary = "Просмотр услуг провайдера",
        description = "Позволяет пользователю посмотреть доступные услуги провайдера с фильтрацией по названию и способу оплаты"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешное получение списка услуг",
                     content = @Content(schema = @Schema(implementation = ProviderDutiesResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<ProviderDutiesResponseDTO> getAllProviderDuties(@RequestParam(required = false) String title, @RequestParam(required = false) Boolean monthlyPayment) 
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth != null && !(auth instanceof AnonymousAuthenticationToken) ? auth.getName() : null;
        return ResponseEntity.status(HttpStatus.OK).body(providerDutyService.getAllProviderDuties(title, monthlyPayment, login));
    }

    @GetMapping("/{dutyID}")
    @Operation
    (
        summary = "Подробнее об услуге",
        description = "Позволяет пользователю получить более подробную информацию об услуге провайдера"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешное получение информации об услуге",
                     content = @Content(schema = @Schema(implementation = ProviderDuty.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> getProviderDutyById(@PathVariable("dutyID") int dutyID) 
    {
        try 
        {
            return ResponseEntity.status(HttpStatus.OK).body(providerDutyService.getProviderDutyById(dutyID));
        }
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    @Operation
    (
        summary = "Добавление новой услуги",
        description = "Позволяет модератору добавить новую услугу провайдера"
    )
    @PreAuthorize("hasAuthority('MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Услуга успешно создана",
                     content = @Content(schema = @Schema(implementation = CreateProviderDutyDTO.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<? extends Object> createProviderDuty(@RequestBody CreateProviderDutyDTO providerDutyDTO) 
    {
        try 
        {
            return ResponseEntity.status(HttpStatus.OK).body(providerDutyService.createProviderDuty(providerDutyDTO));
        }
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{dutyID}/update")
    @Operation
    (
        summary = "Изменение услуги",
        description = "Позволяет модератору изменить информацию об услуге"
    )
    @PreAuthorize("hasAuthority('MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Услуга успешно обновлена",
                     content = @Content(schema = @Schema(implementation = UpdateProviderDutyDTO.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> updateProviderDuty(@PathVariable("dutyID") int dutyID, @RequestBody UpdateProviderDutyDTO providerDutyDTO) 
    {
        try 
        {
            return ResponseEntity.status(HttpStatus.OK).body(providerDutyService.updateProviderDuty(dutyID, providerDutyDTO));
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping("{dutyID}/delete")
    @Operation
    (
        summary = "Удаление услуги",
        description = "Позволяет модератору удалить услугу провайдера"
    )
    @PreAuthorize("hasAuthority('MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Услуга успешно удалена",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String> deleteProviderDuty(@PathVariable("dutyID") int dutyID) 
    {
        try
        {
            providerDutyService.deleteProviderDuty(dutyID);
            return ResponseEntity.status(HttpStatus.OK).body("Услуга " + dutyID + " удалена");
        }
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{dutyID}/add")
    @Operation
    (
        summary = "Добавление в заявку",
        description = "Позволяет пользователю добавить услугу в заявку"
    )
    @PreAuthorize("hasAuthority('BUYER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Услуга успешно добавлена в заявку",
                     content = @Content(schema = @Schema(implementation = ConnectionRequestDTO.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> addProviderDutyToRequest(@PathVariable("dutyID") int dutyID) 
    {
        try 
        {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return ResponseEntity.status(HttpStatus.OK).body(providerDutyService.addProviderDutyToRequest(dutyID, auth.getName()));
        }
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при добавлении услуги в заявку: " + e.getMessage());
        }
    }

    @PostMapping("/{dutyID}/image")
    @Operation
    (
        summary = "Добавление изображения услуги",
        description = "Позволяет модератору добавить изображение для услуги"
    )
    @PreAuthorize("hasAuthority('MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Изображение успешно добавлено",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String> addImageToProviderDuty(@PathVariable("dutyID") int dutyID, @RequestParam("file") MultipartFile file) 
    {
        try 
        {
            providerDutyService.addImageToProviderDuty(dutyID, file);
            return ResponseEntity.status(HttpStatus.OK).body("Картинка была успешно добавлена/изменена");
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при добавлении/изменении изображения услуги: " + e.getMessage());
        }
    }
}
