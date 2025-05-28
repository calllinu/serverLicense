package com.Server.Tests;

import com.Server.exception.EmployeeNotFoundException;
import com.Server.exception.OrganizationNotFoundException;
import com.Server.repository.EmployeeRepository;
import com.Server.repository.FeedbackRepository;
import com.Server.repository.OrganizationRepository;
import com.Server.repository.entity.*;
import com.Server.service.implementation.FeedbackServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackServiceImplTests {

    @Mock
    private FeedbackRepository feedbackRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private OrganizationRepository organizationRepo;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private AutoCloseable mocks;

    @BeforeEach
    void init() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void releaseMocks() throws Exception {
        mocks.close();
    }

    @Test
    void shouldSaveFeedback_whenEmployeeExists() {
        Long userId = 1L;
        Employee foundEmployee = new Employee();
        foundEmployee.setEmployeeId(userId);
        Feedback feedback = new Feedback();

        when(employeeRepo.findByUserUserId(userId)).thenReturn(Optional.of(foundEmployee));

        feedbackService.addFeedback(userId, feedback);

        assertEquals(foundEmployee, feedback.getEmployee());
        verify(feedbackRepo).save(feedback);
    }

    @Test
    void shouldThrowException_whenEmployeeDoesNotExist() {
        Long userId = 1L;
        Feedback feedback = new Feedback();

        when(employeeRepo.findByUserUserId(userId)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> feedbackService.addFeedback(userId, feedback));
        verify(feedbackRepo, never()).save(any());
    }

    @Test
    void shouldReturnFeedbacksForOrganization_whenOrganizationExists() {
        Feedback feedback = new Feedback();
        Employee employee = new Employee();
        employee.setFeedback(feedback);
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setEmployees(List.of(employee));
        Organization organization = new Organization();
        organization.setSubsidiaries(List.of(subsidiary));

        when(organizationRepo.findByOrganizationCode("ORG123")).thenReturn(organization);

        List<Feedback> feedbackList = feedbackService.getFeedbacksForOrganization("ORG123");

        assertEquals(1, feedbackList.size());
        assertEquals(feedback, feedbackList.get(0));
    }

    @Test
    void shouldThrowException_whenOrganizationDoesNotExist() {
        when(organizationRepo.findByOrganizationCode("ORG123")).thenReturn(null);

        assertThrows(OrganizationNotFoundException.class, () -> feedbackService.getFeedbacksForOrganization("ORG123"));
    }
}
