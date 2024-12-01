package com.valer.provider_service.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.valer.provider_service.dto.ConnectionRequestDTO;
import com.valer.provider_service.dto.UpdateConnectionRequestDTO;
import com.valer.provider_service.services.ConnectionRequestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/connection-requests")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name="Заявки клиента провайдера", description="Позволяет получить информацию о заявках клиентов")
public class ConnectionRequestController {

    private final ConnectionRequestService connectionRequestService;

    public ConnectionRequestController(ConnectionRequestService connectionRequestsService) {
        this.connectionRequestService = connectionRequestsService;
    }

    @GetMapping
    @Operation
    (
        summary = "Просмотр заявок клиента",
        description = "Позволяет получить пользователю информацию о его завках, модератору - информацию о всех заявках"
    )
    @ApiResponses
    (value = {
        @ApiResponse(responseCode = "200", description = "Успешное получение списка заявок",
                     content = @Content(schema = @Schema(implementation = ConnectionRequestDTO.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<List<ConnectionRequestDTO>> getAllConnectionRequests
    (
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(required = false) String status
    ) 
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) 
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("MANAGER"))) 
            {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(connectionRequestService.getAllConnectionRequests(null, startDate, endDate, status));
            } 
            else 
            {
                String login = authentication.getName();
                return ResponseEntity.status(HttpStatus.OK)
                    .body(connectionRequestService.getAllConnectionRequests(login, startDate, endDate, status));
            }
        } catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    

    @GetMapping("/{requestID}")
    @Operation(
        summary = "Просмотр конкретной заявки",
        description = "Позволяет получить пользователю информацию о его конкретной завке"
    )
    @PreAuthorize("@userService.isOwnerOfRequest(#requestID, authentication.name)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешное получение заявки",
                     content = @Content(schema = @Schema(implementation = ConnectionRequestDTO.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос", 
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен", 
                     content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<?> getConnectionRequestById(@PathVariable("requestID") int requestID) 
    {
        try 
        {
            ConnectionRequestDTO connectionRequest = connectionRequestService.getConnectionRequestById(requestID);
            return ResponseEntity.status(HttpStatus.OK).body(connectionRequest);
        } catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{requestID}/delete")
    @Operation(
        summary = "Удаление заявки",
        description = "Позволяет пользователю удалить его заявку"
    )
    @PreAuthorize("@userService.isOwnerOfRequest(#requestID, authentication.name)")
    public ResponseEntity<String> deleteConnectionRequest(@PathVariable int requestID) 
    {
        try 
        {
            connectionRequestService.deleteConnectionRequest(requestID);
            return ResponseEntity.status(HttpStatus.OK).body("Заявка на подключение c ID = " + requestID + " успешно удалена");
        } catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{requestID}/update")
    @Operation(
        summary = "Изменение заявки",
        description = "Позволяет пользователю изменить поля 'Заказчик' и 'Номер для связи'"
    )
    @PreAuthorize("@userService.isOwnerOfRequest(#requestID, authentication.name)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешное обновление заявки",
                     content = @Content(schema = @Schema(implementation = ConnectionRequestDTO.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос", 
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен", 
                     content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<?> updateConnectionRequest(@PathVariable int requestID, @RequestBody UpdateConnectionRequestDTO requestDTO) 
    {
        try 
        {
            ConnectionRequestDTO updatedRequest = connectionRequestService.updateConnectionRequest(requestID, requestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedRequest);
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{requestID}/form")
    @Operation(
        summary = "Формирование заявки",
        description = "Позволяет пользователю сформировать заявку"
    )
    @PreAuthorize("@userService.isOwnerOfRequest(#requestID, authentication.name)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешное формирование заявки",
                     content = @Content(schema = @Schema(implementation = ConnectionRequestDTO.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос", 
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен", 
                     content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<?> formConnectionRequest(@PathVariable int requestID) 
    {
        try 
        {
            ConnectionRequestDTO formedRequest = connectionRequestService.formConnectionRequest(requestID);
            return ResponseEntity.status(HttpStatus.OK).body(formedRequest);
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{requestID}/resolve")
    @Operation(
        summary = "Завершение заявки",
        description = "Позволяет модератору отклонить или завершить заявку"
    )
    @PreAuthorize("hasAuthority('MANAGER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешное завершение заявки",
                     content = @Content(schema = @Schema(implementation = ConnectionRequestDTO.class))),
        @ApiResponse(responseCode = "400", description = "Неверный запрос", 
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен", 
                     content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<?> closeConnectionRequest(@PathVariable int requestID, @RequestParam("status") String status) 
    {
        try 
        {
            ConnectionRequestDTO closedRequest = connectionRequestService.closeConnectionRequest(requestID, status);
            return ResponseEntity.status(HttpStatus.OK).body(closedRequest);
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}