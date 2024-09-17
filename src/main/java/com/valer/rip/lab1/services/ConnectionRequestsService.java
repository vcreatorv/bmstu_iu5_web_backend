package com.valer.rip.lab1.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class ConnectionRequestsService {
        private List<Map<String, ? extends Object>> connectionRequests;

        @PostConstruct
        public void init() {
                connectionRequests = List.of(
                        Map.of(
                                "id", "1",
                                "customer", "Организация",
                                "phoneNumber", "+7 (985) 460 48 79",
                                "duties", List.of(
                                        Map.of(
                                                "id", "2",
                                                "title", "Виртуальная АТС",
                                                "imageURL", "http://127.0.0.1:9000/lab1/2.png",
                                                "initialPrice", 350,
                                                "monthlyPayment", true,
                                                "amount", 5,
                                                "description", "количество номеров - "),
                                        Map.of(
                                                "id", "6",
                                                "title", "Аренда двухдиапазонного роутера",
                                                "imageURL", "http://127.0.0.1:9000/lab1/6.png",
                                                "initialPrice", 599,
                                                "monthlyPayment", true,
                                                "amount", 2,
                                                "description", "количество роутеров - ")
                                        )
                                )
                );
        }

        public List<Map<String, ? extends Object>> getConnectionRequests() {
                return connectionRequests;
        }

        public Map<String, ? extends Object> getConnectionRequestById(String id) {
                return connectionRequests.stream()
                        .filter(request -> request.get("id").equals(id))
                        .findFirst().orElse(null);
        }
}
