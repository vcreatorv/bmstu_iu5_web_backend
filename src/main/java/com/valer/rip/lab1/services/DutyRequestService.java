package com.valer.rip.lab1.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.valer.rip.lab1.models.ConnectionRequest;
import com.valer.rip.lab1.models.DutyRequest;
import com.valer.rip.lab1.models.ProviderDuty;
import com.valer.rip.lab1.repositories.ConnectionRequestRepository;
import com.valer.rip.lab1.repositories.DutyRequestRepository;
import com.valer.rip.lab1.repositories.ProviderDutyRepository;


@Service
public class DutyRequestService {
private final ProviderDutyRepository providerDutyRepository;
    private final ConnectionRequestRepository connectionRequestRepository;
    private final DutyRequestRepository dutyRequestRepository;


    public DutyRequestService(ProviderDutyRepository providerDutyRepository, 
                                ConnectionRequestRepository connectionRequestRepository, 
                                DutyRequestRepository dutyRequestRepository) {
        this.providerDutyRepository = providerDutyRepository;
        this.connectionRequestRepository = connectionRequestRepository;
        this.dutyRequestRepository = dutyRequestRepository;
    }

    @Transactional
    public void deleteProviderDutyFromConnectionRequest(int dutyID, int requestID) throws Exception {
        ConnectionRequest connectionRequest = connectionRequestRepository.findById(requestID)
                .orElseThrow(() -> new Exception("Заявка на подключение с ID " + requestID + " не найдена"));
        
        if ("COMPLETED".equals(connectionRequest.getStatus()) || "REJECTED".equals(connectionRequest.getStatus()) || "DELETED".equals(connectionRequest.getStatus())) {
            throw new Exception("Нельзя удалить услуги из завершенной или отклоненной заявки");
        }

        ProviderDuty providerDuty = providerDutyRepository.findById(dutyID)
                .orElseThrow(() -> new Exception("Услуга с ID " + dutyID + " не найдена"));

        dutyRequestRepository.deleteByConnectionRequestAndProviderDuty(connectionRequest, providerDuty);
    }


    @Transactional
    public DutyRequest updateAmountInDutyRequest(int dutyID, int requestID, int amount) throws Exception {
        ConnectionRequest connectionRequest = connectionRequestRepository.findById(requestID)
                .orElseThrow(() -> new Exception("Заявка на подключение с ID " + requestID + " не найдена"));
        
        if ("COMPLETED".equals(connectionRequest.getStatus()) || "REJECTED".equals(connectionRequest.getStatus()) || "DELETED".equals(connectionRequest.getStatus())) {
            throw new Exception("Нельзя удалить услуги из завершенной или отклоненной заявки");
        }

        ProviderDuty providerDuty = providerDutyRepository.findById(dutyID)
                .orElseThrow(() -> new Exception("Услуга с ID " + dutyID + " не найдена"));

        DutyRequest dutyRequest = dutyRequestRepository.findByConnectionRequestAndProviderDuty(connectionRequest, providerDuty)
            .orElseThrow(() -> new Exception("Заявки с такой услугой не существует"));
        
        dutyRequest.setAmount(amount);
        return dutyRequest;
    }
}
