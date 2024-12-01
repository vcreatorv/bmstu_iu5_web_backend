package com.valer.provider_service.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.valer.provider_service.config.MinioConfig;
import com.valer.provider_service.dto.ConnectionRequestDTO;
import com.valer.provider_service.dto.CreateProviderDutyDTO;
import com.valer.provider_service.dto.ProviderDutiesResponseDTO;
import com.valer.provider_service.dto.ProviderDutyInRequestDTO;
import com.valer.provider_service.dto.UpdateProviderDutyDTO;
import com.valer.provider_service.models.ConnectionRequest;
import com.valer.provider_service.models.DutyRequest;
import com.valer.provider_service.models.ProviderDuty;
import com.valer.provider_service.models.User;
import com.valer.provider_service.repositories.ConnectionRequestRepository;
import com.valer.provider_service.repositories.DutyRequestRepository;
import com.valer.provider_service.repositories.ProviderDutyRepository;
import com.valer.provider_service.repositories.UserRepository;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.PostConstruct;


@Service
public class ProviderDutyService {

    private final ProviderDutyRepository providerDutyRepository;
    private final ConnectionRequestRepository connectionRequestRepository;
    private final ConnectionRequestService connectionRequestService;
    private final DutyRequestRepository dutyRequestRepository;
    private final UserRepository userRepository;
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;
    private final ModelMapper modelMapper;


    public ProviderDutyService
    (
        ProviderDutyRepository providerDutyRepository, 
        ConnectionRequestRepository connectionRequestRepository, 
        DutyRequestRepository dutyRequestRepository,
        ConnectionRequestService connectionRequestService,
        UserRepository userRepository,
        MinioClient minioClient,
        MinioConfig minioConfig, 
        ModelMapper modelMapper
    ) 
    {
        this.providerDutyRepository = providerDutyRepository;
        this.connectionRequestRepository = connectionRequestRepository;
        this.dutyRequestRepository = dutyRequestRepository;
        this.connectionRequestService = connectionRequestService;
        this.minioClient = minioClient;
        this.userRepository = userRepository;
        this.minioConfig = minioConfig;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper() 
    {
        modelMapper.getConfiguration()
            .setSkipNullEnabled(true)
            .setMatchingStrategy(MatchingStrategies.STRICT);

        // modelMapper.createTypeMap(ProviderDutyDTO.class, ProviderDuty.class)
        //     .addMappings(mapper -> mapper.skip(ProviderDuty::setId));


        modelMapper.createTypeMap(ProviderDuty.class, ProviderDutyInRequestDTO.class);

        modelMapper.createTypeMap(CreateProviderDutyDTO.class, ProviderDuty.class)
            .addMappings(mapper -> mapper.skip(ProviderDuty::setId));

        modelMapper.createTypeMap(UpdateProviderDutyDTO.class, ProviderDuty.class)
            .addMappings(mapper -> mapper.skip(ProviderDuty::setId));

        modelMapper.createTypeMap(ProviderDuty.class, CreateProviderDutyDTO.class);

        modelMapper.createTypeMap(ProviderDuty.class, UpdateProviderDutyDTO.class);
    }

    @Transactional(readOnly = true)
    public ProviderDutiesResponseDTO getAllProviderDuties(String titleFilter, Boolean monthlyPayment, String login) 
    {
        ProviderDutiesResponseDTO providerDutiesResponseDTO = new ProviderDutiesResponseDTO();
        providerDutiesResponseDTO.setItemsInCart(0);
        providerDutiesResponseDTO.setConnectionRequestId(null);

        if (login != null) 
        {
            User user = userRepository.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            ConnectionRequest connectionRequest = connectionRequestRepository.findFirstByClientAndStatus(user, "DRAFT").orElse(null);

            if(connectionRequest != null)
            {
                providerDutiesResponseDTO.setConnectionRequestId(connectionRequest.getId());
                
                int itemsInCart = dutyRequestRepository.findByConnectionRequestEquals(connectionRequest).size();
                providerDutiesResponseDTO.setItemsInCart(itemsInCart);
                
            }
        }

        List<ProviderDuty> providerDuties;
        if (titleFilter != null && monthlyPayment != null) 
        {
            providerDuties = providerDutyRepository.findByTitleContainingIgnoreCaseAndActiveTrueAndMonthlyPayment(titleFilter, monthlyPayment);
        } 
        else if (titleFilter != null) 
        {
            providerDuties = providerDutyRepository.findByTitleContainingIgnoreCaseAndActiveTrue(titleFilter);
        } 
        else if (monthlyPayment != null) 
        {
            providerDuties = providerDutyRepository.findByMonthlyPaymentAndActiveTrue(monthlyPayment);
        } 
        else 
        {
            providerDuties = providerDutyRepository.findByActiveTrue();
        }
    
        providerDutiesResponseDTO.setProviderServiceList(providerDuties);
        return providerDutiesResponseDTO;
    }

    @Transactional(readOnly = true)
    public ProviderDuty getProviderDutyById(int dutyID) throws Exception 
    {
        ProviderDuty providerDuty = providerDutyRepository.findById(dutyID)
                .orElseThrow(() -> new Exception("Услуга с ID " + dutyID + " не найдена"));
        
        if (providerDuty.getActive())
        {
            return providerDutyRepository.findById(dutyID).get();
        }
        else
        {
            throw new Exception("Услуга с ID " + dutyID + " не найдена");
        }
    }

    @Transactional
    public CreateProviderDutyDTO createProviderDuty(CreateProviderDutyDTO providerDutyDTO) throws Exception {
        try 
        {
            ProviderDuty providerDuty = new ProviderDuty();

            modelMapper.map(providerDutyDTO, providerDuty);
            providerDutyRepository.save(providerDuty);

            modelMapper.map(providerDuty, providerDutyDTO);

            return providerDutyDTO;
        } 
        catch (DataIntegrityViolationException e) 
        {
            throw new Exception("Ошибка целостности данных: возможно, такая услуга уже существует:");
        } 
        catch (Exception e) 
        {
            throw new Exception("Произошла ошибка при сохранении услуги");
        }
    }

    @Transactional
    public UpdateProviderDutyDTO updateProviderDuty(int dutyID, UpdateProviderDutyDTO providerDutyDTO) throws Exception 
    {
        ProviderDuty providerDuty = providerDutyRepository.findById(dutyID).orElseThrow(() -> new Exception("Услуга с ID " + dutyID + " не найдена"));

        if (!providerDuty.getActive()) 
        {
            throw new Exception("Невозможно обновить неактивную услугу");
        }

        modelMapper.map(providerDutyDTO, providerDuty);
        providerDutyRepository.save(providerDuty);    
        modelMapper.map(providerDuty, providerDutyDTO);

        return providerDutyDTO;
    }

    @Transactional
    public void deleteProviderDuty(int dutyID) throws Exception 
    {
        Optional<ProviderDuty> providerDutyToDelete = providerDutyRepository.findById(dutyID);
        
        if(!providerDutyToDelete.isPresent()) 
        {
            throw new Exception("У провайдера нет такой услуги");
        } 
        else 
        {
            if (providerDutyToDelete.get().getImgUrl() != null) 
            {
                deleteImageFromMinio(providerDutyToDelete.get().getImgUrl());
            }
            providerDutyRepository.deleteById(dutyID);
        }
    }

    private void deleteImageFromMinio(String imageUrl) throws Exception 
    {
        try 
        {
            String objectName = extractObjectNameFromUrl(imageUrl);
            minioClient.removeObject(
            RemoveObjectArgs.builder()
                    .bucket("lab1")
                    .object(objectName)
                    .build());
        } 
        catch (Exception e) 
        {
            throw new Exception("Ошибка при удалении изображения из Minio: " + e.getMessage());
        }
    }

    private String extractObjectNameFromUrl(String imageUrl) 
    {
        String[] parts = imageUrl.split("/");
        return parts[parts.length - 1];
    }

    @Transactional
    public ConnectionRequestDTO addProviderDutyToRequest(int dutyID, String login) throws Exception 
    {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new Exception("Пользователь не найден"));

        ProviderDuty providerDuty = providerDutyRepository.findById(dutyID)
                .orElseThrow(() -> new Exception("Услуга не найдена"));

        ConnectionRequest request = connectionRequestRepository.findFirstByClientAndStatus(user, "DRAFT")
                .orElseGet(() -> 
                {
                    ConnectionRequest newRequest = new ConnectionRequest();
                    newRequest.setClient(user);
                    newRequest.setStatus("DRAFT");
                    newRequest.setCreationDatetime(LocalDateTime.now());
                    return connectionRequestRepository.save(newRequest);
                });

        List<DutyRequest> dutyRequests = dutyRequestRepository.findByConnectionRequestEquals(request);

        boolean dutyExists = dutyRequests.stream()
                .anyMatch(dr -> dr.getProviderDuty().getId() == dutyID);

        if (!dutyExists) 
        {
            DutyRequest dutyRequest = new DutyRequest();
            dutyRequest.setProviderDuty(providerDuty);
            dutyRequest.setConnectionRequest(request);
            dutyRequest.setAmount(1);
            dutyRequestRepository.save(dutyRequest);
            return connectionRequestService.convertToDTO(connectionRequestRepository.save(request), true);
        }

        return connectionRequestService.convertToDTO(request, true);
    }

    @Transactional
    public ProviderDuty addImageToProviderDuty(int dutyID, MultipartFile image) throws Exception 
    {
        ProviderDuty providerDuty = providerDutyRepository.findById(dutyID).orElseThrow(() -> new Exception("Услуга не найдена"));

        String fileName = providerDuty.getId() + getFileExtension(image.getOriginalFilename());

        try 
        {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(fileName)
                    .stream(image.getInputStream(), image.getSize(), -1)
                    .contentType(image.getContentType())
                    .build()
            );

            // String imageUrl = minioConfig.getUrl() + "/" + minioConfig.getBucketName() + "/" + fileName;
            String imageUrl = "/" + minioConfig.getBucketName() + "/" + fileName;
            providerDuty.setImgUrl(imageUrl);
            return providerDutyRepository.save(providerDuty);
        } 
        catch (Exception e) 
        {
            throw new Exception("Ошибка при загрузке изображения: " + e.getMessage());
        }
    }

    public ProviderDutyInRequestDTO convertToDTO(ProviderDuty providerDuty)
    {
        return modelMapper.map(providerDuty, ProviderDutyInRequestDTO.class);
    }

    private String getFileExtension(String fileName) 
    {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
