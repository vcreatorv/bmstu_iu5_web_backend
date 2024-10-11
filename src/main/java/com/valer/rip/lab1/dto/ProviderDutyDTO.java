package com.valer.rip.lab1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderDutyDTO {
    private int id;
    private String title;
    // private String description;
    //private Optional<Boolean> active;
    //private Optional<Boolean> monthlyPayment;
    private Boolean active;
    private int price;
    private Boolean monthlyPayment;
    private String unit;
    private String amountDescription;
    private int amount;


    // public ProviderDutyDTO() {
    //     this.active = Optional.empty();
    //     this.monthlyPayment = Optional.empty();
    // }

    // public Optional<Boolean> getActive() {
    //     return active;
    // }

    // public void setActive(Boolean active) {
    //     this.active = Optional.ofNullable(active);
    // }

    // public Optional<Boolean> getMonthlyPayment() {
    //     return monthlyPayment;
    // }

    // public void setMonthlyPayment(Boolean monthlyPayment) {
    //     this.monthlyPayment = Optional.ofNullable(monthlyPayment);
    // }
    
}