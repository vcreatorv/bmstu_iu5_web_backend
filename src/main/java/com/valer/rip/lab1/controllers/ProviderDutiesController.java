package com.valer.rip.lab1.controllers;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.valer.rip.lab1.models.ConnectionRequest;
import com.valer.rip.lab1.models.ProviderDuty;
import com.valer.rip.lab1.services.ConnectionRequestService;
import com.valer.rip.lab1.services.ProviderDutyService;

@Controller
@RequestMapping("/duties")
public class ProviderDutiesController {

    private final ProviderDutyService providerDutyService;
    private final ConnectionRequestService connectionRequestService;

    public ProviderDutiesController(ProviderDutyService providerDutyService, 
                                    ConnectionRequestService connectionRequestService) {
        this.providerDutyService = providerDutyService;
        this.connectionRequestService = connectionRequestService;
    }

    @ModelAttribute("cart")
    public Optional<ConnectionRequest> getDraftConnectionRequest() {
        return connectionRequestService.getDraftConnectionRequestByUserId(1);
    }
    

    @GetMapping
    public String getProviderDuties(Model model) {
        model.addAttribute("duties", providerDutyService.getAllProviderDuties());
        return "duties";
    }

    @GetMapping("/{id}")
    public String getProviderDutyById(@PathVariable("id") int id, Model model) {
        Optional<ProviderDuty> providerDuty = providerDutyService.getProviderDutyById(id);
        if (providerDuty.isPresent()) {
            model.addAttribute("duty", providerDuty.get());
        } else {
           System.out.println("Provider duty not found");
        }
        return "duty";
    }

    @GetMapping("/search")
    public String findDutyByTitle(@RequestParam("serviceTitle") String serviceTitle, Model model) {
        if (serviceTitle == null || serviceTitle.isEmpty()) {
            model.addAttribute("duties", providerDutyService.getAllProviderDuties());
        } else {
            model.addAttribute("duties", providerDutyService.findProviderDutiesByTitle(serviceTitle));
            model.addAttribute("serviceTitle", serviceTitle.toLowerCase());
        }
        return "duties";
    }
}
