package com.valer.rip.lab1.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "duties_requests", uniqueConstraints = {@UniqueConstraint(columnNames = {"provider_duty", "connection_request"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DutyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "provider_duty", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProviderDuty providerDuty;

    @ManyToOne
    @JoinColumn(name = "connection_request", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ConnectionRequest connectionRequest;

    @Column(nullable = false, columnDefinition = "int default 1")
    private int amount = 1;

}