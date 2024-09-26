package com.valer.rip.lab1.controllers;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.valer.rip.lab1.models.ConnectionRequest;
import com.valer.rip.lab1.services.ConnectionRequestsService;


@Controller
@RequestMapping("/connection_requests")
public class ConnectionRequestsController {

    private final ConnectionRequestsService connectionRequestsService;

    public ConnectionRequestsController(ConnectionRequestsService connectionRequestsService) {
        this.connectionRequestsService = connectionRequestsService;
    }

    @GetMapping("/{id}")
    public String getConnectionRequestById(@PathVariable("id") int id, Model model) {
        
        if (id == 0) {
            return "redirect:/duties";
        }

        Optional<ConnectionRequest> connectionRequest = connectionRequestsService.getConnectionRequestById(id);
        if (connectionRequest.isPresent()) {
            model.addAttribute("connection_request", connectionRequest.get());
        } 
        else {
            model.addAttribute("errorMessage", "Connection request not found");
        }
        return "cart";
    }

    @PostMapping("/add_provider_duty")
    public String addProviderDutyToRequest(@RequestParam("dutyId") int dutyId) {
        connectionRequestsService.addProviderDutyToRequest(dutyId);
        return "redirect:/duties";
    }
    
}
