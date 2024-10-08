package com.valer.rip.lab1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.valer.rip.lab1.models.ConnectionRequest;
import com.valer.rip.lab1.models.DutyRequest;
import com.valer.rip.lab1.models.ProviderDuty;

@Repository
public interface DutyRequestRepository extends JpaRepository<DutyRequest, Integer> {

    List<DutyRequest> findByConnectionRequestEquals(ConnectionRequest connectionRequest);

    void deleteByConnectionRequestAndProviderDuty(ConnectionRequest connectionRequest, ProviderDuty providerDuty);

    Optional<DutyRequest> findByConnectionRequestAndProviderDuty(ConnectionRequest connectionRequest, ProviderDuty providerDuty);

}