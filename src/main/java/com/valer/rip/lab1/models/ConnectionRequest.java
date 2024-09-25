package com.valer.rip.lab1.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "connection_requests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 10, nullable = false)
    private String status;

    @Column(length = 255, nullable = false)
    private String consumer;

    @Column(name = "phone_number", length = 255, nullable = false)
    private String phoneNumber;

    @Column(name = "creation_datetime", nullable = false)
    private LocalDateTime creationDatetime;

    @Column(name = "formation_datetime")
    private LocalDateTime formationDatetime;

    @Column(name = "completion_datetime")
    private LocalDateTime completionDatetime;

    @ManyToOne
    @JoinColumn(name = "manager", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User manager;

    @ManyToOne
    @JoinColumn(name = "client", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User client;

    // @OneToMany(mappedBy = "connectionRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<DutyRequest> duties = new ArrayList<>();

    @OneToMany(mappedBy = "connectionRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DutyRequest> dutyRequests;

}
