package com.valer.provider_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.valer.provider_service.models.ConnectionRequest;
import com.valer.provider_service.models.DutyRequest;
import com.valer.provider_service.models.ProviderDuty;

@Repository
public interface DutyRequestRepository extends JpaRepository<DutyRequest, Integer> {

    List<DutyRequest> findByConnectionRequestEquals(ConnectionRequest connectionRequest);

    void deleteByConnectionRequestAndProviderDuty(ConnectionRequest connectionRequest, ProviderDuty providerDuty);

    Optional<DutyRequest> findByConnectionRequestAndProviderDuty(ConnectionRequest connectionRequest, ProviderDuty providerDuty);
}