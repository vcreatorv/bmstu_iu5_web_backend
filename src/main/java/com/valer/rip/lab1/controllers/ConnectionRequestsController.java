package com.valer.rip.lab1.controllers;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.valer.rip.lab1.services.ConnectionRequestsService;


@Controller
@RequestMapping("/connection-requests")
public class ConnectionRequestsController {

    private final ConnectionRequestsService connectionRequestsService;

    public ConnectionRequestsController(ConnectionRequestsService connectionRequestsService) {
        this.connectionRequestsService = connectionRequestsService;
    }

    @GetMapping("/{id}")
    public String getConnectionRequestById(@PathVariable("id") String id, Model model) {
        Map<String, ? extends Object> connectionRequest = connectionRequestsService.getConnectionRequestById("1");
        model.addAttribute("connection_request", connectionRequest);
        return "cart";
    } 
}
