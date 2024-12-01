package com.valer.provider_service.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.valer.provider_service.dto.ConnectionRequestDTO;
import com.valer.provider_service.dto.ProviderDutyInRequestDTO;
import com.valer.provider_service.dto.UpdateConnectionRequestDTO;
import com.valer.provider_service.models.ConnectionRequest;
import com.valer.provider_service.models.DutyRequest;
import com.valer.provider_service.models.User;
import com.valer.provider_service.repositories.ConnectionRequestRepository;
import com.valer.provider_service.repositories.DutyRequestRepository;
import com.valer.provider_service.repositories.UserRepository;
import com.valer.provider_service.utils.ConnectionRequestSpecifications;

import jakarta.annotation.PostConstruct;


@Service
public class ConnectionRequestService 
{
    private final ConnectionRequestRepository connectionRequestRepository;
    private final DutyRequestRepository dutyRequestRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ConnectionRequestService
    (
        ConnectionRequestRepository connectionRequestRepository, 
        UserRepository userRepository,
        DutyRequestRepository dutyRequestRepository,
        ModelMapper modelMapper
    ) 
    {
        this.connectionRequestRepository = connectionRequestRepository;
        this.userRepository = userRepository;
        this.dutyRequestRepository = dutyRequestRepository;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper() 
    {
        modelMapper.getConfiguration()
        .setSkipNullEnabled(true)
        .setMatchingStrategy(MatchingStrategies.STRICT);
        
        modelMapper.createTypeMap(ConnectionRequest.class, ConnectionRequestDTO.class)
        .addMappings
        (mapper -> {
                mapper.skip(ConnectionRequestDTO::setManager);
                mapper.skip(ConnectionRequestDTO::setClient);
            }
        );

        modelMapper.createTypeMap(UpdateConnectionRequestDTO.class, ConnectionRequest.class); 
    }

    @Transactional(readOnly = true)
    public List<ConnectionRequestDTO> getAllConnectionRequests
    (
        String login, 
        LocalDate startDate, 
        LocalDate endDate, 
        String status
    ) throws Exception 
    {
        User client = null;

        if (login != null) 
        {
            client = userRepository.findByLogin(login).orElseThrow(() -> new Exception("Пользователь не найден!"));
        }
        
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atStartOfDay() : null;

        Specification<ConnectionRequest> spec = ConnectionRequestSpecifications.filterByParams(client, startDateTime, endDateTime, status);
        List<ConnectionRequest> requests = connectionRequestRepository.findAll(spec);
        
        return requests.stream()
            .map(request -> convertToDTO(request, false))
            .collect(Collectors.toList());
    }


    @Transactional
    public void deleteConnectionRequest(int requestID) throws Exception 
    {
        ConnectionRequest connectionRequest = connectionRequestRepository.findById(requestID)
                .orElseThrow(() -> new Exception("Такой заявки на подключение нет"));
        connectionRequest.setStatus("DELETED");
    }

    @Transactional(readOnly = true)
    public ConnectionRequestDTO getConnectionRequestById(int requestID) throws Exception 
    {
        ConnectionRequest connectionRequest = connectionRequestRepository.findById(requestID)
                .orElseThrow(() -> new Exception("Заявка на подключение с ID " + requestID + " не найдена"));
        
        if ("DELETED".equals(connectionRequest.getStatus())) 
        {
            throw new Exception("Заявка была удалена");
        }
        
        return convertToDTO(connectionRequest, true);
    }

    @Transactional
    public ConnectionRequestDTO updateConnectionRequest(int requestID, UpdateConnectionRequestDTO requestDTO) throws Exception 
    {
        ConnectionRequest connectionRequest = connectionRequestRepository.findById(requestID)
                .orElseThrow(() -> new Exception("Заявка на подключение с ID " + requestID + " не найдена"));

        if (!"DRAFT".equals(connectionRequest.getStatus())) 
        {
            throw new Exception("Невозможно обновить нечерновую заявку");
        }

        if (requestDTO.getConsumer() != null && !"".equals(requestDTO.getConsumer())) 
        {
            connectionRequest.setConsumer(requestDTO.getConsumer());
        }

        if (requestDTO.getPhoneNumber() != null && !"".equals(requestDTO.getPhoneNumber())) 
        {
            connectionRequest.setPhoneNumber(requestDTO.getPhoneNumber());
        }

        connectionRequestRepository.save(connectionRequest);
        return convertToDTO(connectionRequest, false);
    }

    @Transactional
    public ConnectionRequestDTO formConnectionRequest(int requestID) throws Exception 
    {
        ConnectionRequest connectionRequest = connectionRequestRepository.findById(requestID)
                .orElseThrow(() -> new Exception("Заявка на подключение с ID " + requestID + " не найдена"));

        if (!"DRAFT".equals(connectionRequest.getStatus())) 
        {
            throw new Exception("Заявка не является черновиком");
        }

        if (connectionRequest.getConsumer() != null && connectionRequest.getPhoneNumber() != null) 
        {
            connectionRequest.setStatus("FORMED");
            connectionRequest.setFormationDatetime(LocalDateTime.now());
            ConnectionRequest updatedRequest = connectionRequestRepository.save(connectionRequest);
            return convertToDTO(updatedRequest, false);
        } 
        else 
        {
            throw new Exception("Поля consumer и phoneNumber должны быть заполнены");
        }
    }

    @Transactional
    public ConnectionRequestDTO closeConnectionRequest(int requestID, String status) throws Exception 
    {
        ConnectionRequest connectionRequest = connectionRequestRepository.findById(requestID)
                .orElseThrow(() -> new Exception("Заявка на подключение с ID " + requestID + " не найдена"));

        if (!"FORMED".equals(connectionRequest.getStatus())) 
        {
            throw new Exception("Заявка не сформирована");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin = authentication.getName();
        User user = userRepository.findByLogin(userLogin).orElseThrow(() -> new Exception("Пользователь не найден!"));

        connectionRequest.setManager(user);

        connectionRequest.setStatus(status);
        connectionRequest.setCompletionDatetime(LocalDateTime.now());
        connectionRequest.setTotalPrice(countTotalPrice(connectionRequest));
        
        ConnectionRequest updatedRequest = connectionRequestRepository.save(connectionRequest);
        return convertToDTO(updatedRequest, false);
    }
    
    public int countTotalPrice(ConnectionRequest connectionRequest) 
    {
        List<DutyRequest> dutyRequestList =  dutyRequestRepository.findByConnectionRequestEquals(connectionRequest);
        return dutyRequestList.stream()
            .mapToInt(dutyRequest -> dutyRequest.getAmount() * dutyRequest.getProviderDuty().getPrice())
            .sum();
    }

    public ConnectionRequestDTO convertToDTO(ConnectionRequest connectionRequest, boolean includeProviderDuties) 
    {
        ConnectionRequestDTO dto = modelMapper.map(connectionRequest, ConnectionRequestDTO.class);

        if (connectionRequest.getManager() != null) 
        {
            dto.setManager(connectionRequest.getManager().getLogin());
        }
        if (connectionRequest.getClient() != null) 
        {
            dto.setClient(connectionRequest.getClient().getLogin());
        }
        
        if (includeProviderDuties) 
        {
            List<ProviderDutyInRequestDTO> dutyDtoList = dutyRequestRepository.findByConnectionRequestEquals(connectionRequest)
                .stream()
                .map
                    (dutyRequest -> 
                        {
                            ProviderDutyInRequestDTO providerDutyDTO = new ProviderDutyInRequestDTO();
                            BeanUtils.copyProperties(dutyRequest.getProviderDuty(), providerDutyDTO);
                            providerDutyDTO.setAmount(dutyRequest.getAmount());
                            return providerDutyDTO;
                        }
                    )
                .collect(Collectors.toList());

            dto.setProviderServiceList(dutyDtoList);
        }
        return dto;
    }

}
