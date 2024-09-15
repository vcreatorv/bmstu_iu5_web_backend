package com.valer.rip.lab1.models;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offer {
    private String id;

    private String name;

    private String imageURL;

    private String description;

    private int price;

    private LocalDateTime createdAt;

    private boolean monthlyPayment;

    private List<Attribute> attributes;

    
    public boolean getMonthlyPayment() {
        return monthlyPayment;
    }
}
