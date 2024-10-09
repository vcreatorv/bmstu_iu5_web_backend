package com.valer.rip.lab1.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.valer.rip.lab1.dto.ConnectionRequestDTO;
import com.valer.rip.lab1.models.ConnectionRequest;
import com.valer.rip.lab1.services.ConnectionRequestService;


@Controller
@RequestMapping("/api/connection-requests")
public class ConnectionRequestController {

    private final ConnectionRequestService connectionRequestService;

    public ConnectionRequestController(ConnectionRequestService connectionRequestsService) {
        this.connectionRequestService = connectionRequestsService;
    }

    @GetMapping
    public ResponseEntity<List<ConnectionRequest>> getAllConnectionRequests() {
        return ResponseEntity.status(HttpStatus.OK).body(connectionRequestService.getAllConnectionRequests());
    }

    @GetMapping("/{requestID}")
    public ResponseEntity<? extends Object> getConnectionRequestById(@PathVariable("requestID") int requestID) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(connectionRequestService.getConnectionRequestById(requestID));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{requestID}/delete")
    public ResponseEntity<String> deleteConnectionRequest(@PathVariable int requestID) {
        try {
            connectionRequestService.deleteConnectionRequest(requestID);
            return ResponseEntity.status(HttpStatus.OK).body("Заявка на подключение c ID = " + requestID + " успешно удалена");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{requestID}/update")
    public ResponseEntity<? extends Object> updateConnectionRequest(@PathVariable int requestID, @ModelAttribute ConnectionRequestDTO requestDTO) {
        try {
            ConnectionRequest updatedRequest = connectionRequestService.updateConnectionRequest(requestID, requestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{requestID}/form")
    public ResponseEntity<? extends Object> formConnectionRequest(@PathVariable int requestID) {
        try {
            ConnectionRequest formedRequest = connectionRequestService.formConnectionRequest(requestID);
            return ResponseEntity.status(HttpStatus.OK).body(formedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{requestID}/close-request")
    public ResponseEntity<? extends Object> closeConnectionRequest(@PathVariable int requestID, @RequestParam("status") String status) {
        try {
            ConnectionRequest closedRequest = connectionRequestService.closeConnectionRequest(requestID, status);
            return ResponseEntity.status(HttpStatus.OK).body(closedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
