package com.valer.rip.lab1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.valer.rip.lab1.models.ProviderDuty;

@Repository
public interface ProviderDutyRepository extends JpaRepository<ProviderDuty, Integer> {
    List<ProviderDuty> findByActiveTrue();
    List<ProviderDuty> findByTitleContainingIgnoreCaseAndActiveTrue(String title);
    List<ProviderDuty> findByMonthlyPaymentAndActiveTrue(Boolean monthlyPayment);
    List<ProviderDuty> findByTitleContainingIgnoreCaseAndActiveTrueAndMonthlyPayment(String title, Boolean monthlyPayment);
}