package com.valer.rip.lab1.services;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        public List<ConnectionRequest> getAllConnectionRequests() {
                return connectionRequestRepository.findAll();
        }

        @Transactional(readOnly = true)
        public Optional<ConnectionRequest> getConnectionRequestById(int id) {
                Optional<ConnectionRequest> connectionRequestOpt = connectionRequestRepository.findById(id);
                connectionRequestOpt.ifPresent(connectionRequest -> {
                        connectionRequest.getDutyRequests()
                                        .sort(Comparator.comparing(dr -> dr.getProviderDuty().getId()));
                });
                return connectionRequestOpt;
        }

        @Transactional(readOnly = true)
        public Optional<ConnectionRequest> getDraftConnectionRequestByUserId(int userId) {
                return Optional.ofNullable(connectionRequestRepository.findDraftConnectionRequestByUserId(userId));
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
                                .orElseThrow(() -> new RuntimeException("ConnectionRequest not found"));

                DutyRequest dutyRequestToUpdate = connectionRequest.getDutyRequests().stream()
                                .filter(dr -> dr.getId() == dutyRequestId)
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException(
                                                "DutyRequest not found in this ConnectionRequest"));

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
                                .orElseThrow(() -> new RuntimeException("ConnectionRequest not found"));
                connectionRequest.setConsumer(consumer);
                connectionRequestRepository.save(connectionRequest);
        }

        @Transactional
        public void updatePhoneNumber(int connectionRequestId, String phoneNumber) {
                ConnectionRequest connectionRequest = connectionRequestRepository.findById(connectionRequestId)
                                .orElseThrow(() -> new RuntimeException("ConnectionRequest not found"));
                connectionRequest.setPhoneNumber(phoneNumber);
                connectionRequestRepository.save(connectionRequest);
        }
}


// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.valer.rip.lab1.models.ConnectionRequest;
// import com.valer.rip.lab1.models.DutyRequest;
// import com.valer.rip.lab1.models.ProviderDuty;
// import com.valer.rip.lab1.models.User;
// import com.valer.rip.lab1.repositories.ConnectionRequestRepository;
// import com.valer.rip.lab1.repositories.DutyRequestRepository;
// import com.valer.rip.lab1.repositories.ProviderDutyRepository;
// import com.valer.rip.lab1.repositories.UserRepository;

// @Service
// public class ConnectionRequestService {
// private final ConnectionRequestRepository connectionRequestRepository;
// private final ProviderDutyRepository providerDutyRepository;
// private final DutyRequestRepository dutyRequestRepository;
// private final UserRepository userRepository;

// public ConnectionRequestService(ConnectionRequestRepository
// connectionRequestRepository,
// ProviderDutyRepository providerDutyRepository, DutyRequestRepository
// dutyRequestRepository,
// UserRepository userRepository) {
// this.connectionRequestRepository = connectionRequestRepository;
// this.providerDutyRepository = providerDutyRepository;
// this.dutyRequestRepository = dutyRequestRepository;
// this.userRepository = userRepository;
// }

// public List<ConnectionRequest> getConnectionRequests() {
// return connectionRequestRepository.findAll();
// }

// public Optional<ConnectionRequest> getConnectionRequestById(int id) {
// return Optional.ofNullable(connectionRequestRepository.findById(id));
// }

// public Optional<ConnectionRequest> getDraftConnectionRequestByUser(String
// username) {
// return
// Optional.ofNullable(connectionRequestRepository.getDraftConnectionRequestByUser(username));
// }

// @Transactional
// public void addProviderDutyToRequest(int dutyId) {
// Optional<ConnectionRequest> draftRequestOpt =
// getDraftConnectionRequestByUser("vcreatorv");
// ConnectionRequest draftRequest = draftRequestOpt.orElseGet(() -> {
// User client = getUserByLogin("vcreatorv"); // Получите пользователя по имени
// return connectionRequestRepository.createConnectionRequest(client); //
// Передаем клиента
// });

// boolean exists = draftRequest.getDutyRequests()
// .stream()
// .anyMatch(dutyRequest -> dutyRequest.getProviderDuty().getId() == dutyId);

// if (!exists) {
// formProviderRequest(draftRequest, dutyId);
// }
// }

// public void formProviderRequest(ConnectionRequest request, int dutyId) {
// Optional<ProviderDuty> providerDutyOpt =
// Optional.ofNullable(providerDutyRepository.findById(dutyId));

// providerDutyOpt.ifPresent(providerDuty -> {
// DutyRequest newDutyRequest = new DutyRequest();
// newDutyRequest.setProviderDuty(providerDuty);
// newDutyRequest.setConnectionRequest(request);
// request.getDutyRequests().add(newDutyRequest);

// // Здесь не обязательно сохранять, так как JPA отслеживает изменения
// // dutyRequestRepository.save(newDutyRequest);
// });
// }

// private User getUserByLogin(String login) {
// // Получите пользователя из репозитория или сервиса
// return userRepository.findUserByLogin(login); // Или аналогичный метод
// }

// }
