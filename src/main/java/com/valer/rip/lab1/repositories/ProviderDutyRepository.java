package com.valer.rip.lab1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.valer.rip.lab1.models.ProviderDuty;

@Repository
public interface ProviderDutyRepository extends JpaRepository<ProviderDuty, Integer> {
    List<ProviderDuty> findByTitleContaining(String title);

    List<ProviderDuty> findByActiveTrue();
}