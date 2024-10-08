package com.valer.rip.lab1.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.valer.rip.lab1.models.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRequestDTO {
    private int id;
    private String status;
    private String consumer;
    private String phoneNumber;
    private LocalDateTime creationDatetime;
    private LocalDateTime formationDatetime;
    private LocalDateTime completionDatetime;
    private int totalPrice;
    // private Integer managerId;
    // private Integer clientId;
    private User manager;
    private User client;
    private List<ProviderDutyDTO> duties;

    // public Integer setClient() {
    //     return clientId;
    // }

    // public Integer setManager() {
    //     return managerId;
    // }
}
