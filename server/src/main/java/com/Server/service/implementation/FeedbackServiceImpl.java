package com.Server.service.implementation;

import com.Server.exception.EmployeeNotFoundException;
import com.Server.repository.EmployeeRepository;
import com.Server.repository.FeedbackRepository;
import com.Server.repository.entity.Employee;
import com.Server.repository.entity.Feedback;
import com.Server.service.FeedbackService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EmployeeRepository employeeRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository,
                               EmployeeRepository employeeRepository) {
        this.feedbackRepository = feedbackRepository;
        this.employeeRepository = employeeRepository;
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
}
