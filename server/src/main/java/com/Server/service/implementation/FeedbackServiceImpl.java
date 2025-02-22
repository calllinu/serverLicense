package com.Server.service.implementation;

import com.Server.exception.EmployeeNotFoundException;
import com.Server.exception.OrganizationNotFoundException;
import com.Server.repository.EmployeeRepository;
import com.Server.repository.FeedbackRepository;
import com.Server.repository.OrganizationRepository;
import com.Server.repository.entity.Employee;
import com.Server.repository.entity.Feedback;
import com.Server.repository.entity.Organization;
import com.Server.service.interfaces.FeedbackService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EmployeeRepository employeeRepository;

    private final OrganizationRepository organizationRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository,
                               EmployeeRepository employeeRepository,
                               OrganizationRepository organizationRepository) {
        this.feedbackRepository = feedbackRepository;
        this.employeeRepository = employeeRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public void addFeedback(Long employeeId, Feedback feedback) {
        if (employeeRepository.findByUserUserId(employeeId).isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found");
        }
        Employee employee = employeeRepository.findByUserUserId(employeeId).get();
        feedback.setEmployee(employee);
        feedbackRepository.save(feedback);
    }

    @Override
    public Map<String, Object> getAllFeedbacks(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Feedback> pagedResult = feedbackRepository.findAll(paging);

        List<Map<String, Object>> feedbackDetails = pagedResult.getContent().stream().map(feedback -> {
            Map<String, Object> feedbackMap = new HashMap<>();
            feedbackMap.put("feedback", feedback);

            Employee employee = employeeRepository.findByEmployeeId(feedback.getEmployee().getEmployeeId());
            if (employee != null) {
                Map<String, Object> employeeDetails = new HashMap<>();

                Map<String, Object> subsidiaryDetails = new HashMap<>();
                subsidiaryDetails.put("subsidiaryCode", employee.getSubsidiary().getSubsidiaryCode());
                subsidiaryDetails.put("country", employee.getSubsidiary().getCountry());
                subsidiaryDetails.put("city", employee.getSubsidiary().getCity());
                subsidiaryDetails.put("address", employee.getSubsidiary().getAddress());

                Map<String, Object> organizationDetails = new HashMap<>();
                organizationDetails.put("organizationCode", employee.getSubsidiary().getOrganization().getOrganizationCode());
                organizationDetails.put("name", employee.getSubsidiary().getOrganization().getName());
                organizationDetails.put("industry", employee.getSubsidiary().getOrganization().getIndustry());

                employeeDetails.put("subsidiaryDetails", subsidiaryDetails);
                employeeDetails.put("organizationDetails", organizationDetails);

                feedbackMap.put("employeeDetails", employeeDetails);
                feedbackMap.put("feedbackId", feedback.getFeedbackId());
            }
            return feedbackMap;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("feedbacks", feedbackDetails);
        response.put("currentPage", pagedResult.getNumber());
        response.put("totalItems", pagedResult.getTotalElements());
        response.put("totalPages", pagedResult.getTotalPages());

        return response;
    }

    @Override
    public List<Feedback> getFeedbacksForOrganization(String organizationCode) {
        Organization organization = organizationRepository.findByOrganizationCode(organizationCode);
        if (organization != null) {
            return organization.getSubsidiaries().stream()
                    .flatMap(subsidiary -> subsidiary.getEmployees().stream())
                    .map(Employee::getFeedback)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        else {
            throw new OrganizationNotFoundException("Organization not found");
        }
    }
}
