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
import com.valer.rip.lab1.services.ConnectionRequestService;


@Controller
@RequestMapping("/connection_requests")
public class ConnectionRequestController {

    private final int userId = 1;

    private final ConnectionRequestService connectionRequestService;

    public ConnectionRequestController(ConnectionRequestService connectionRequestsService) {
        this.connectionRequestService = connectionRequestsService;
    }

    @GetMapping("/{id}")
    public String getConnectionRequestById(@PathVariable("id") int id, Model model) {
        
        if (id == 0) {
            return "redirect:/duties";
        }

        Optional<ConnectionRequest> connectionRequest = connectionRequestService.getConnectionRequestById(id);
        if (connectionRequest.isPresent()) {
            model.addAttribute("connection_request", connectionRequest.get());
            model.addAttribute("total_price", connectionRequestService.getTotalPriceOfRequest(connectionRequest.get()));
            System.out.println(connectionRequestService.getTotalPriceOfRequest(connectionRequest.get()));
        } 
        else {
            System.out.println("Connection request not found");
            model.addAttribute("total_price", 0);
        }
        return "cart";
    }

    @PostMapping("/add_provider_duty")
    public String addProviderDutyToRequest(@RequestParam("dutyId") int dutyId) {
        connectionRequestService.addProviderDutyToRequest(dutyId, this.userId);
        return "redirect:/duties";
    }
    
}
