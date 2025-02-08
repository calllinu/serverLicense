package com.Server.service.implementation;

import com.Server.repository.EmployeeRepository;
import com.Server.repository.RegistrationRequestRepository;
import com.Server.repository.UserRepository;
import com.Server.repository.dto.RegistrationResponseDTO;
import com.Server.repository.entity.*;
import com.Server.service.EmailService;
import com.Server.service.RequestService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    private final RegistrationRequestRepository registrationRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;


    public RequestServiceImpl(RegistrationRequestRepository registrationRequestRepository,
                              UserRepository userRepository,
                              EmailService emailService,
                              EmployeeRepository employeeRepository) {
        this.registrationRequestRepository = registrationRequestRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.employeeRepository = employeeRepository;
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

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(request.getPassword());
        user.setRole(Role.EMPLOYEE);
        userRepository.save(user);

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setFullName(request.getFullName());
        employee.setSubsidiary(request.getSubsidiary());
        employee.setYearsOfExperience(calculateYearsOfExperience(employee));
        employeeRepository.save(employee);


        request.setStatus(RequestStatus.APPROVED);
        registrationRequestRepository.save(request);

        Map<String, Object> userTemplateData = new HashMap<>();
        userTemplateData.put("fullName", user.getFullName());
        userTemplateData.put("message", "Your registration request has been approved. You can now log in using your credentials.");

        emailService.sendTemplateEmail(user.getEmail(),
                "[SafetyNet AI] Registration Approved",
                "user-notification.ftl",
                userTemplateData);
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

        Map<String, Object> userTemplateData = new HashMap<>();
        userTemplateData.put("fullName", request.getFullName());
        userTemplateData.put("message", "Your registration request has been declined. Please contact support for further information.");

        emailService.sendTemplateEmail(request.getEmail(),
                "[SafetyNet AI] Registration Declined",
                "user-notification.ftl",
                userTemplateData);
    }

    protected Integer calculateYearsOfExperience(Employee employee) {
        if (employee.getDateOfHiring() != null) {
            return Period.between(employee.getDateOfHiring(), LocalDate.now()).getYears();
        }
        return 0;
    }
}
