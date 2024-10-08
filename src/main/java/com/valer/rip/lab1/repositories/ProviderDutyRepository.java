package com.valer.rip.lab1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valer.rip.lab1.models.ProviderDuty;

@Repository
public interface ProviderDutyRepository extends JpaRepository<ProviderDuty, Integer> {
    
    @Query("FROM ProviderDuty pd WHERE pd.active=true AND LOWER(pd.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<ProviderDuty> findByTitleOrderById(String title);

    List<ProviderDuty> findByActiveTrueOrderById();
}