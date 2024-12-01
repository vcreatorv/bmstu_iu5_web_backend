package com.valer.provider_service.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.valer.provider_service.dto.ConnectionRequestPositionDTO;
import com.valer.provider_service.models.ConnectionRequest;
import com.valer.provider_service.models.DutyRequest;
import com.valer.provider_service.models.ProviderDuty;
import com.valer.provider_service.repositories.ConnectionRequestRepository;
import com.valer.provider_service.repositories.DutyRequestRepository;
import com.valer.provider_service.repositories.ProviderDutyRepository;


@Service
public class DutyRequestService 
{
    private final ProviderDutyRepository providerDutyRepository;
    private final ConnectionRequestRepository connectionRequestRepository;
    private final DutyRequestRepository dutyRequestRepository;
    private final ModelMapper modelMapper;

    private final ProviderDutyService providerDutyService;
    private final ConnectionRequestService connectionRequestService;

    public DutyRequestService
    (
        ProviderDutyRepository providerDutyRepository, 
        ConnectionRequestRepository connectionRequestRepository, 
        DutyRequestRepository dutyRequestRepository,
        ProviderDutyService providerDutyService,
        ConnectionRequestService connectionRequestService,
        ModelMapper modelMapper
    )
    {
        this.providerDutyRepository = providerDutyRepository;
        this.connectionRequestRepository = connectionRequestRepository;
        this.dutyRequestRepository = dutyRequestRepository;
        this.providerDutyService = providerDutyService;
        this.connectionRequestService = connectionRequestService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void deleteProviderDutyFromConnectionRequest(int dutyID, int requestID) throws Exception 
    {
        ConnectionRequest connectionRequest = connectionRequestRepository.findById(requestID)
                .orElseThrow(() -> new Exception("Заявка на подключение с ID " + requestID + " не найдена"));
        
        if ("COMPLETED".equals(connectionRequest.getStatus()) || "REJECTED".equals(connectionRequest.getStatus()) || "DELETED".equals(connectionRequest.getStatus())) 
        {
            throw new Exception("Нельзя удалить услуги из завершенной или отклоненной заявки");
        }

        ProviderDuty providerDuty = providerDutyRepository.findById(dutyID)
                .orElseThrow(() -> new Exception("Услуга с ID " + dutyID + " не найдена"));

        dutyRequestRepository.deleteByConnectionRequestAndProviderDuty(connectionRequest, providerDuty);
    }


    @Transactional
    public ConnectionRequestPositionDTO updateAmountInDutyRequest(int dutyID, int requestID, int amount) throws Exception 
    {
        ConnectionRequest connectionRequest = connectionRequestRepository.findById(requestID)
                .orElseThrow(() -> new Exception("Заявка на подключение с ID " + requestID + " не найдена"));
        
        if (!"DRAFT".equals(connectionRequest.getStatus()) && !"FORMED".equals(connectionRequest.getStatus())) 
        {
            throw new Exception("Нельзя изменить количество в несформированной заявке или не являющейся черновиком");
        }

        ProviderDuty providerDuty = providerDutyRepository.findById(dutyID)
                .orElseThrow(() -> new Exception("Услуга с ID " + dutyID + " не найдена"));

        DutyRequest dutyRequest = dutyRequestRepository.findByConnectionRequestAndProviderDuty(connectionRequest, providerDuty)
            .orElseThrow(() -> new Exception("Заявки с такой услугой не существует"));
        
        dutyRequest.setAmount(amount);
        DutyRequest updatedDutyRequest = dutyRequestRepository.save(dutyRequest);
        
        return convertToDTO(updatedDutyRequest);
    }

    public ConnectionRequestPositionDTO convertToDTO(DutyRequest dutyRequest)
    {
        ConnectionRequestPositionDTO dto = modelMapper.map(dutyRequest, ConnectionRequestPositionDTO.class);
        
        dto.setConnectionRequest(connectionRequestService.convertToDTO(dutyRequest.getConnectionRequest(), false));
        dto.setProviderDuty(providerDutyService.convertToDTO(dutyRequest.getProviderDuty()));

        if (dto.getProviderDuty() != null) 
        {
            dto.getProviderDuty().setAmount(dutyRequest.getAmount());
        }
        
        return dto;
    }
}
