package com.valer.rip.lab1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.valer.rip.lab1.models.ProviderDuty;
import com.valer.rip.lab1.repositories.ProviderDutyRepository;

@Service
public class ProviderDutyService {

    private final ProviderDutyRepository providerDutyRepository;

    public ProviderDutyService(ProviderDutyRepository providerDutyRepository) {
        this.providerDutyRepository = providerDutyRepository;
    }

    @Transactional(readOnly = true)
    public List<ProviderDuty> getAllProviderDuties() {
        return providerDutyRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public Optional<ProviderDuty> getProviderDutyById(int id) {
        return providerDutyRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ProviderDuty> findProviderDutiesByTitle(String title) {
        return providerDutyRepository.findByTitleContaining(title.toLowerCase());
    }
}
