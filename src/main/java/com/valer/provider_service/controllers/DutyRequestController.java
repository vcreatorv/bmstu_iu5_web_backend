package com.valer.provider_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.valer.provider_service.dto.ConnectionRequestPositionDTO;
import com.valer.provider_service.services.DutyRequestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/duties-requests")
@SecurityRequirement(name = "Bearer Authentication")
@Tag
(
    name="Услуги в заявке",
    description="Позволяет изменять состав заявки"
)
public class DutyRequestController 
{

    private final DutyRequestService dutyRequestService;

    public DutyRequestController( DutyRequestService dutyRequestService) 
    {
        this.dutyRequestService = dutyRequestService;
    }
   
    @DeleteMapping("/{dutyID}/{requestID}/delete")
    @Operation
    (
        summary = "Удаление услуги из заявки",
        description = "Позволяет пользователю удалить услугу из заявки"
    )
    @PreAuthorize("hasAuthority('BUYER') and @userService.isOwnerOfRequest(#requestID, authentication.name)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Услуга успешно удалена из заявки",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String> deleteProviderDutyFromConnectionRequest(@PathVariable("dutyID") int dutyID, @PathVariable("requestID") int requestID) 
    {
        try 
        {
            dutyRequestService.deleteProviderDutyFromConnectionRequest(dutyID, requestID);
            return ResponseEntity.status(HttpStatus.OK).body("Услуга с ID = " + dutyID + " успешно удалена из заявки с ID = " + requestID);
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении услуги с ID = " + dutyID + "из заявки c ID = " + requestID + ": " + e.getMessage());
        }
    }

    @PutMapping("/{dutyID}/{requestID}/update")
    @Operation
    (
        summary = "Изменение параметров услуги",
        description = "Позволяет пользователю изменить поле 'количество' в услуге в заявке"
    )
    @PreAuthorize("hasAuthority('BUYER') and @userService.isOwnerOfRequest(#requestID, authentication.name)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Количество услуги успешно обновлено",
                     content = @Content(schema = @Schema(implementation = ConnectionRequestPositionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> updateAmountInDutyRequest
    (   
        @PathVariable("dutyID") int dutyID, 
        @PathVariable("requestID") int requestID, 
        @RequestParam("amount") int amount
    ) 
    {
        try 
        {
            return ResponseEntity.status(HttpStatus.OK).body(dutyRequestService.updateAmountInDutyRequest(dutyID, requestID, amount));
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ошибка при изменении поля amount услуги с ID = " + dutyID + 
                    " в заявке c ID = " + requestID + ": " + e.getMessage());
        }
    }
}
