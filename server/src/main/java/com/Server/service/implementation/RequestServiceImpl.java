package com.Server.service.implementation;

import com.Server.repository.RegistrationRequestRepository;
import com.Server.repository.dto.RegistrationResponseDTO;
import com.Server.repository.entity.RegistrationRequest;
import com.Server.repository.entity.RequestStatus;
import com.Server.service.RequestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    private final RegistrationRequestRepository registrationRequestRepository;

    public RequestServiceImpl(RegistrationRequestRepository registrationRequestRepository) {
        this.registrationRequestRepository = registrationRequestRepository;
    }

    @Override
    public List<RegistrationResponseDTO> getAllRequestsByAdmin(Long adminId) {
        List<RegistrationRequest> requests = registrationRequestRepository.findByAdminId(adminId);

        return requests.stream()
                .map(request -> new RegistrationResponseDTO(
                        request.getRequestId(),
                        request.getUsername(),
                        request.getEmail(),
                        request.getFullName(),
                        request.getSubsidiary(),
                        request.getStatus()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void acceptRegistrationRequest(Long requestId) {
        RegistrationRequest request = registrationRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request ID"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Request is not in a pending state.");
        }

        request.setStatus(RequestStatus.APPROVED);
        registrationRequestRepository.save(request);
    }

    @Override
    public void declineRegistrationRequest(Long requestId) {
        RegistrationRequest request = registrationRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request ID"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Request is not in a pending state.");
        }

        request.setStatus(RequestStatus.REJECTED);
        registrationRequestRepository.save(request);
    }
}
