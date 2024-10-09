package com.valer.rip.lab1.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
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
    private String status = "DRAFT";

    @Column(length = 50, nullable = false)
    private String consumer = "Организация";

    @Column(name = "phone_number", length = 18, nullable = false)
    private String phoneNumber = "+7 (985) 460 48 79";

    @Column(name = "creation_datetime", nullable = false)
    @CreationTimestamp
    private LocalDateTime creationDatetime;

    @Column(name = "formation_datetime")
    private LocalDateTime formationDatetime;

    @Column(name = "completion_datetime")
    private LocalDateTime completionDatetime;

    @Column(name = "total_price")
    private int totalPrice;

    @ManyToOne
    @JoinColumn(name = "manager")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User manager;

    @ManyToOne
    @JoinColumn(name = "client", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User client;

    @OneToMany(mappedBy = "connectionRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DutyRequest> dutyRequests = new ArrayList<>();;

    public ConnectionRequest(User client) {
        this.client = client;
    }
}
