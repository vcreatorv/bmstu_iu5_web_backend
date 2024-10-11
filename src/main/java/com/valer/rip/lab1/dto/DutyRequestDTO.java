package com.valer.rip.lab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DutyRequestDTO {
    private int id;
    private ProviderDutyDTO providerDuty;
    private ConnectionRequestDTO connectionRequest;
}