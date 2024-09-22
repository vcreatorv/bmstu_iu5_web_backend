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
                connectionRequests = new ArrayList<>(List.of(
                                new HashMap<>(Map.of(
                                                "id", "1",
                                                "customer", "Организация",
                                                "phoneNumber", "+7 (985) 460 48 79",
                                                "duties", new ArrayList<>(List.of(
                                                                new HashMap<>(Map.of(
                                                                                "id", "2",
                                                                                "title", "Виртуальная АТС",
                                                                                "imageURL",
                                                                                "http://127.0.0.1:9000/lab1/2.png",
                                                                                "initialPrice", 350,
                                                                                "monthlyPayment", true,
                                                                                "amount", 5)),
                                                                new HashMap<>(Map.of(
                                                                                "id", "6",
                                                                                "title",
                                                                                "Аренда двухдиапазонного роутера",
                                                                                "imageURL",
                                                                                "http://127.0.0.1:9000/lab1/6.png",
                                                                                "initialPrice", 599,
                                                                                "monthlyPayment", true,
                                                                                "amount", 2))))))));
        }

        public List<Map<String, ? extends Object>> getConnectionRequests() {
                return connectionRequests;
        }

        public Map<String, ? extends Object> getConnectionRequestById(String id) {
                Map<String, ? extends Object> cart = connectionRequests.stream()
                                .filter(request -> request.get("id").equals(id))
                                .findFirst().orElse(null);

                List<Map<String, ? extends Object>> providerDuties = providerDutiesService.getProviderDuties();

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> duties = (List<Map<String, Object>>) cart.get("duties");

                duties.forEach(duty -> {
                        String dutyId = (String) duty.get("id");
                        providerDuties.stream()
                                        .filter(providerDuty -> dutyId.equals(providerDuty.get("id")))
                                        .findFirst()
                                        .ifPresent(providerDuty -> duty.put("amountDescription",
                                                        (String) providerDuty.get("amountDescription")));
                });

                return cart;
        }
}
