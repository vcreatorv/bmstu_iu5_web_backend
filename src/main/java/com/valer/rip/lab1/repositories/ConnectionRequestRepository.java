package com.valer.rip.lab1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.valer.rip.lab1.models.ConnectionRequest;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Integer> {

    @Query("FROM ConnectionRequest cr WHERE cr.client.id = :userId AND cr.status = 'DRAFT'")
    Optional<ConnectionRequest> findDraftConnectionRequestByUserId(Integer userId);
}