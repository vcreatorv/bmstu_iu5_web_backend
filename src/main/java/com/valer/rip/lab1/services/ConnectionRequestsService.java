package com.valer.rip.lab1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.valer.rip.lab1.models.ConnectionRequest;
import com.valer.rip.lab1.models.DutyRequest;
import com.valer.rip.lab1.models.ProviderDuty;
import com.valer.rip.lab1.repositories.ConnectionRequestRepository;
import com.valer.rip.lab1.repositories.DutyRequestRepository;
import com.valer.rip.lab1.repositories.ProviderDutyRepository;


@Service
public class ConnectionRequestsService {
        private final ConnectionRequestRepository connectionRequestRepository;
        private final ProviderDutyRepository providerDutyRepository;
        private final DutyRequestRepository dutyRequestRepository;

        public ConnectionRequestsService(ConnectionRequestRepository connectionRequestRepository,
                        ProviderDutyRepository providerDutyRepository, DutyRequestRepository dutyRequestRepository) {
                this.connectionRequestRepository = connectionRequestRepository;
                this.providerDutyRepository = providerDutyRepository;
                this.dutyRequestRepository = dutyRequestRepository;
        }

        public List<ConnectionRequest> getConnectionRequests() {
                return connectionRequestRepository.findAll();
        }

        public Optional<ConnectionRequest> getConnectionRequestById(int id) {
                return Optional.ofNullable(connectionRequestRepository.findById(id));
        }

        public Optional<ConnectionRequest> getDraftConnectionRequestByUser(String username) {
                return Optional.ofNullable(connectionRequestRepository.getDraftConnectionRequestByUser(username));
        }

        @Transactional
        public void addProviderDutyToRequest(int dutyId) {
                ConnectionRequest draftRequest = getDraftConnectionRequestByUser("vcreatorv").get();
                
                if (draftRequest == null) {
                        draftRequest = connectionRequestRepository.createConnectionRequest();
                        formProviderRequest(draftRequest, dutyId);
                } else {
                        boolean exists = draftRequest.getDutyRequests()
                                        .stream()
                                        .anyMatch(dutyRequest -> dutyRequest.getProviderDuty().getId() == dutyId);
                        if (!exists) {
                                formProviderRequest(draftRequest, dutyId);
                        }
                }
        }


        public void formProviderRequest(ConnectionRequest request, int dutyId) {
                DutyRequest newDutyRequest = new DutyRequest();
                ProviderDuty providerDuty = providerDutyRepository.findById(dutyId);
                newDutyRequest.setProviderDuty(providerDuty);
                newDutyRequest.setConnectionRequest(request);
                request.getDutyRequests().add(newDutyRequest);
                dutyRequestRepository.save(newDutyRequest);
                connectionRequestRepository.save(request);

                for (DutyRequest dr : request.getDutyRequests()){
                        System.out.println(dr.getId());
                }
        }
}
