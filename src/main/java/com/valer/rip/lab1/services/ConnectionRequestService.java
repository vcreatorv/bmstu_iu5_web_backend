package com.valer.rip.lab1.services;

import java.util.Comparator;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.valer.rip.lab1.models.ConnectionRequest;
import com.valer.rip.lab1.models.DutyRequest;
import com.valer.rip.lab1.models.ProviderDuty;
import com.valer.rip.lab1.models.User;
import com.valer.rip.lab1.repositories.ConnectionRequestRepository;
import com.valer.rip.lab1.repositories.ProviderDutyRepository;
import com.valer.rip.lab1.repositories.UserRepository;

@Service
public class ConnectionRequestService {
        private final ConnectionRequestRepository connectionRequestRepository;
        private final ProviderDutyRepository providerDutyRepository;
        private final UserRepository userRepository;
        private final JdbcTemplate jdbcTemplate;

        public ConnectionRequestService(ConnectionRequestRepository connectionRequestRepository,
                        ProviderDutyRepository providerDutyRepository, UserRepository userRepository,
                        JdbcTemplate jdbcTemplate) {
                this.connectionRequestRepository = connectionRequestRepository;
                this.providerDutyRepository = providerDutyRepository;
                this.userRepository = userRepository;
                this.jdbcTemplate = jdbcTemplate;
        }

        @Transactional(readOnly = true)
        public ConnectionRequest getConnectionRequestById(int id) {
                Optional<ConnectionRequest> connectionRequestOpt = connectionRequestRepository.findById(id);
                
                ConnectionRequest connectionRequest = connectionRequestOpt.orElseThrow(() -> 
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Заявка на подключение с id = " + id + " не найдена"));

                if ("DELETED".equals(connectionRequest.getStatus())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Заявка на подключение с id = " + id + " удалена");
                }

                connectionRequest.getDutyRequests()
                .sort(Comparator.comparing(dr -> dr.getProviderDuty().getId()));

                return connectionRequest;
        }

        @Transactional(readOnly = true)
        public Optional<ConnectionRequest> getDraftConnectionRequestByUserId(int userId) {
                return connectionRequestRepository.findDraftConnectionRequestByUserId(userId);
        }

        public int getTotalPriceOfRequest(ConnectionRequest connectionRequest) {
                int totalPrice = connectionRequest.getDutyRequests()
                                .stream()
                                .mapToInt(request -> request.getProviderDuty().getPrice() * request.getAmount())
                                .sum();
                return totalPrice;
        }

        @Transactional
        public void addProviderDutyToRequest(int dutyId, int userId) {
                ConnectionRequest draftRequest = getDraftConnectionRequestByUserId(userId)
                                .orElseGet(() -> createNewDraftRequest(userId));

                if (!isDutyAlreadyInRequest(draftRequest, dutyId)) {
                        addDutyToRequest(draftRequest, dutyId);
                }
        }

        private ConnectionRequest createNewDraftRequest(int userId) {
                Optional<User> client = userRepository.findById(userId);
                return connectionRequestRepository.save(new ConnectionRequest(client.get()));
        }

        private boolean isDutyAlreadyInRequest(ConnectionRequest request, int dutyId) {
                return request.getDutyRequests().stream()
                                .anyMatch(dutyRequest -> dutyRequest.getProviderDuty().getId() == dutyId);
        }

        private void addDutyToRequest(ConnectionRequest request, int dutyId) {
                Optional<ProviderDuty> providerDuty = providerDutyRepository.findById(dutyId);

                DutyRequest newDutyRequest = new DutyRequest();
                newDutyRequest.setProviderDuty(providerDuty.get());
                newDutyRequest.setConnectionRequest(request);
                request.getDutyRequests().add(newDutyRequest);

                connectionRequestRepository.save(request);
        }

        @Transactional
        public void updateProviderDutyAmount(int connectionRequestId, int dutyRequestId, int amount) {
                ConnectionRequest connectionRequest = connectionRequestRepository.findById(connectionRequestId)
                                .orElseThrow(() -> new RuntimeException("Заявка на подключение не найдена"));

                DutyRequest dutyRequestToUpdate = connectionRequest.getDutyRequests().stream()
                                .filter(dr -> dr.getId() == dutyRequestId)
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException(
                                                "Заявка с данной услугой не найдена"));

                dutyRequestToUpdate.setAmount(amount);
        }

        @Transactional
        public void updateConnectionRequestStatusToDeleted(int connectionRequestId) {
                String sql = "UPDATE connection_requests SET status = 'DELETED' WHERE id = ?";
                jdbcTemplate.update(sql, connectionRequestId);
        }

        @Transactional
        public void updateConsumer(int connectionRequestId, String consumer) {
                ConnectionRequest connectionRequest = connectionRequestRepository.findById(connectionRequestId)
                                .orElseThrow(() -> new RuntimeException("Заявка на подключение не найдена"));
                connectionRequest.setConsumer(consumer);
                connectionRequestRepository.save(connectionRequest);
        }

        @Transactional
        public void updatePhoneNumber(int connectionRequestId, String phoneNumber) {
                ConnectionRequest connectionRequest = connectionRequestRepository.findById(connectionRequestId)
                                .orElseThrow(() -> new RuntimeException("Заявка на подключение не найдена"));
                connectionRequest.setPhoneNumber(phoneNumber);
                connectionRequestRepository.save(connectionRequest);
        }
}