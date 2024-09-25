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
import com.valer.rip.lab1.services.ConnectionRequestsService;
import com.valer.rip.lab1.services.ProviderDutiesService;

@Controller
@RequestMapping("/duties")
public class ProviderDutiesController {

    private final ProviderDutiesService providerDutiesService;
    private final ConnectionRequestsService connectionRequestsService;

    public ProviderDutiesController(ProviderDutiesService providerDutiesService, 
                                    ConnectionRequestsService connectionRequestsService) {
        this.providerDutiesService = providerDutiesService;
        this.connectionRequestsService = connectionRequestsService;
    }

    @ModelAttribute("cart")
    public Optional<ConnectionRequest> getConnectionRequestById() {
        return connectionRequestsService.getConnectionRequestById(1);
    }

    @GetMapping
    public String getProviderDuties(Model model) {
        model.addAttribute("duties", providerDutiesService.getProviderDuties());
        return "duties";
    }

    @GetMapping("/{id}")
    public String getProviderDutyById(@PathVariable("id") int id, Model model) {
        Optional<ProviderDuty> providerDuty = providerDutiesService.getProviderDutyById(id);
        if (providerDuty.isPresent()) {
            model.addAttribute("duty", providerDuty.get());
        } else {
            model.addAttribute("errorMessage", "Provider duty not found");
        }
        return "duty";
    }

    @GetMapping("/search")
    public String findDutyByTitle(@RequestParam("serviceTitle") String serviceTitle, Model model) {
        if (serviceTitle == null || serviceTitle.isEmpty()) {
            model.addAttribute("duties", providerDutiesService.getProviderDuties());
        } else {
            model.addAttribute("duties", providerDutiesService.findDutyByTitle(serviceTitle));
            model.addAttribute("serviceTitle", serviceTitle.toLowerCase());
        }
        return "duties";
    }
}


// private final ProviderDutiesService providerDutiesService;

    // private final ConnectionRequestsService connectionRequestsService;

    // public ProviderDutiesController(ProviderDutiesService providerDutiesService, ConnectionRequestsService connectionRequestsService) {
    //     this.providerDutiesService = providerDutiesService;
    //     this.connectionRequestsService = connectionRequestsService;
    // }

    // @ModelAttribute("cart")
    // public Map<String, ? extends Object> getConnectionRequestById() {
    //     return connectionRequestsService.getConnectionRequestById("1");
    // }

    // @GetMapping()
    // public String getProviderDuties(Model model) {
    //     model.addAttribute("duties", providerDutiesService.getProviderDuties());
    //     return "duties";
    // }

    // @GetMapping("/{id}")
    // public String getProviderDutyById(@PathVariable("id") String id, Model model) {
    //     model.addAttribute("duty", providerDutiesService.getProviderDutyById(id));
    //     return "duty";
    // }

    // @GetMapping("/search")
    // public String searchProviderDuties(@RequestParam("serviceTitle") String serviceTitle, Model model) {
    //     if (serviceTitle == null || serviceTitle.isEmpty()) {
    //         model.addAttribute("duties", providerDutiesService.getProviderDuties());
    //     } else {
    //         model.addAttribute("duties", providerDutiesService.searchProviderDuties(serviceTitle));
    //         model.addAttribute("serviceTitle", serviceTitle.toLowerCase());
    //     }
    //     return "duties";
    // }