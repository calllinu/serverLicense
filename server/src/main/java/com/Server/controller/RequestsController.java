package com.Server.controller;

import com.Server.repository.dto.registerRequestDTOs.RegistrationResponseDTO;
import com.Server.service.interfaces.RequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registration-requests")
public class RequestsController {

    private final RequestService registrationRequestService;

    public RequestsController(RequestService registrationRequestService) {
        this.registrationRequestService = registrationRequestService;
    }

    @GetMapping("/admin/{adminId}")
    public List<RegistrationResponseDTO> getRequestsByAdmin(@PathVariable Long adminId) {
        return registrationRequestService.getAllRequestsByAdmin(adminId);
    }

    @PutMapping("/accept/{requestId}")
    public void acceptRequest(@PathVariable Long requestId) {
        registrationRequestService.acceptRegistrationRequest(requestId);
    }

    @PutMapping("/decline/{requestId}")
    public void declineRequest(@PathVariable Long requestId) {
        registrationRequestService.declineRegistrationRequest(requestId);
    }
}
