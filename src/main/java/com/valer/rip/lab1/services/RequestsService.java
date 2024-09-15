package com.valer.rip.lab1.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class RequestsService {
        private List<Map<String, ? extends Object>> requests;

        @PostConstruct
        public void init() {
                requests = List.of(
                        Map.of(
                                "id", 1,
                                "customer", "Организация",
                                "phoneNumber", "+7 (985) 460 48 79",
                                "positions", List.of(
                                        Map.of(
                                                "id", "2",
                                                "title", "Виртуальная АТС",
                                                "imageURL", "http://127.0.0.1:9000/lab1/virtual-ats.webp",
                                                "initialPrice", 350,
                                                "monthlyPayment", true,
                                                "amount", 5,
                                                "description", "количество номеров - "),
                                        Map.of(
                                                "id", "6",
                                                "title", "Аренда двухдиапазонного роутера",
                                                "imageURL", "http://127.0.0.1:9000/lab1/router_rent.webp",
                                                "initialPrice", 599,
                                                "monthlyPayment", true,
                                                "amount", 2,
                                                "description", "количество роутеров - ")
                                        )
                                )
                );

                //int amount = requests.get(0).get("positions").get(0).get("amount"); 
        }

        public List<Map<String, ? extends Object>> getRequests() {
                return requests;
        }
}
