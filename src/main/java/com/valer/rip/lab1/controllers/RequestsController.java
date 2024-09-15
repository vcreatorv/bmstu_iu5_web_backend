package com.valer.rip.lab1.controllers;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.valer.rip.lab1.services.RequestsService;


@Controller
@RequestMapping("/requests")
public class RequestsController {

    private final RequestsService requestsService;

    public RequestsController(RequestsService requestsService) {
        this.requestsService = requestsService;
    }

    @GetMapping()
    public String getRequest(Model model) {
        Map<String, ? extends Object> request = requestsService.getRequests().get(0);
        model.addAttribute("request", request);
        return "cart";
    } 
}
