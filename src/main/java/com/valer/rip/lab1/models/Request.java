package com.valer.rip.lab1.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    private String id;

    private String customer;

    private String phoneNumber;
    
}
