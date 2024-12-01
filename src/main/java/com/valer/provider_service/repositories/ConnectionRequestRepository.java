package com.valer.provider_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.valer.provider_service.models.ConnectionRequest;
import com.valer.provider_service.models.User;


@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Integer>, JpaSpecificationExecutor<ConnectionRequest> {
    Optional<ConnectionRequest> findFirstByClientAndStatus(User client, String status);

}

