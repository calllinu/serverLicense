package com.Server.service;

import com.Server.repository.dto.RegistrationResponseDTO;

import java.util.List;

public interface RequestService {
    List<RegistrationResponseDTO> getAllRequestsByAdmin(Long adminId);
    void acceptRegistrationRequest(Long requestId);
    void declineRegistrationRequest(Long requestId);
}
