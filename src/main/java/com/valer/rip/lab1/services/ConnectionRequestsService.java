package com.valer.rip.lab1.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class ConnectionRequestsService {
        private List<Map<String, ? extends Object>> connectionRequests;

        private final ProviderDutiesService providerDutiesService;

        public ConnectionRequestsService(ProviderDutiesService providerDutiesService) {
                this.providerDutiesService = providerDutiesService;
        }
        
        @PostConstruct
        public void init() {
                List<Map<String, ? extends Object>> providerDuties = providerDutiesService.getProviderDuties();
                connectionRequests = new ArrayList<>(List.of(
                                new HashMap<>(Map.of(
                                                "id", "1",
                                                "customer", "Организация",
                                                "phoneNumber", "+7 (985) 460 48 79",
                                                "duties", new ArrayList<>(List.of(
                                                                new HashMap<>(Map.of(
                                                                                "providerDuty", providerDuties.get(1),
                                                                                "amount", 5)),
                                                                new HashMap<>(Map.of(
                                                                                "providerDuty", providerDuties.get(5),
                                                                                "amount", 2))))))));
        }

        public List<Map<String, ? extends Object>> getConnectionRequests() {
                return connectionRequests;
        }

        public Map<String, ? extends Object> getConnectionRequestById(String id) {
                Map<String, ? extends Object> cart = connectionRequests.stream()
                                .filter(request -> request.get("id").equals(id))
                                .findFirst().orElse(null);

                return cart;
        }
}
