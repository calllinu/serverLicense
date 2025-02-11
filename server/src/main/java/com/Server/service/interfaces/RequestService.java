package com.Server.service.interfaces;

import com.Server.repository.dto.registerRequestDTOs.RegistrationResponseDTO;

import java.util.List;

public interface RequestService {
    List<RegistrationResponseDTO> getAllRequestsByAdmin(Long adminId);
    void acceptRegistrationRequest(Long requestId);
    void declineRegistrationRequest(Long requestId);
}
