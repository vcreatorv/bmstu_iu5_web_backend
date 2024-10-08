package com.valer.rip.lab1.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valer.rip.lab1.models.ConnectionRequest;
import com.valer.rip.lab1.models.User;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Integer> {
    Optional<ConnectionRequest> findByClientAndStatus(User client, String status);

    @Query("SELECT cr FROM ConnectionRequest cr WHERE cr.status != 'DELETED' AND cr.status != 'DRAFT'")
    List<ConnectionRequest> findAllExceptDeletedAndDraft();


}
