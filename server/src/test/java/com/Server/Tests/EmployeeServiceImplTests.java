package com.Server.Tests;

import com.Server.repository.EmployeeRepository;
import com.Server.repository.entity.Employee;
import com.Server.repository.entity.Subsidiary;
import com.Server.repository.entity.User;
import com.Server.repository.entity.enums.Qualification;
import com.Server.service.implementation.EmployeeServiceImpl;
import com.Server.service.interfaces.EmailService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private AutoCloseable mocks;

    @BeforeEach
    void initMocks() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void releaseMocks() throws Exception {
        mocks.close();
    }

    @Test
    void shouldReturnEmployee_whenEmployeeExists() {
        Long userId = 1L;
        Employee mockEmployee = new Employee();

        when(employeeRepository.findByUserUserId(userId)).thenReturn(Optional.of(mockEmployee));

        Employee result = employeeService.getEmployeeByUserId(userId);

        assertEquals(mockEmployee, result);
    }

    @Test
    void shouldThrowException_whenEmployeeNotFound() {
        Long userId = 1L;

        when(employeeRepository.findByUserUserId(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> employeeService.getEmployeeByUserId(userId));
    }

    @Test
    void shouldSkipEmail_whenEmployeeAlreadyComplete() {
        Long userId = 1L;
        Employee completeEmployee = filledEmployee();

        when(employeeRepository.findByUserUserId(userId)).thenReturn(Optional.of(completeEmployee));

        employeeService.updateEmployee(userId, completeEmployee);

        verify(emailService, never()).sendTemplateEmail(any(), any(), any(), any());
    }

    @Test
    void shouldReturnTrue_whenEmployeeHasNullFields() {
        Long userId = 1L;
        Employee incompleteEmployee = emptyEmployee();

        when(employeeRepository.findByUserUserId(userId)).thenReturn(Optional.of(incompleteEmployee));

        assertTrue(employeeService.hasNullFields(userId));
    }

    @Test
    void shouldReturnFalse_whenEmployeeHasNoNullFields() {
        Long userId = 1L;
        Employee completeEmployee = filledEmployee();

        when(employeeRepository.findByUserUserId(userId)).thenReturn(Optional.of(completeEmployee));

        assertFalse(employeeService.hasNullFields(userId));
    }

    @Test
    void shouldDeleteEmployeeByUserId() {
        Long userId = 1L;

        employeeService.deleteEmployee(userId);

        verify(employeeRepository).deleteByUserUserId(userId);
    }

    @Test
    void shouldReturnEmployeesBySubsidiaryId() {
        Long subsidiaryId = 1L;
        List<Employee> mockEmployees = List.of(new Employee(), new Employee());

        when(employeeRepository.findAllBySubsidiaryId(subsidiaryId)).thenReturn(mockEmployees);

        List<Employee> result = employeeService.getEmployeesBySubsidiaryId(subsidiaryId);

        assertEquals(2, result.size());
    }

    private Employee emptyEmployee() {
        Employee employee = new Employee();
        User user = new User();
        user.setEmail("calin.lupascu@gmail.com");
        employee.setUser(user);
        return employee;
    }

    private Employee filledEmployee() {
        Employee employee = new Employee();
        employee.setFullName("Calin Lupascu");
        employee.setEmployeeCNP("1234567890123");
        employee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        employee.setDateOfHiring(LocalDate.of(2015, 1, 1));
        employee.setYearsOfExperience(10);
        employee.setQualification(Qualification.PROFESSIONAL_QUALIFICATION);

        User user = new User();
        user.setEmail("calin.lupascu@gmail");
        employee.setUser(user);

        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setSubsidiaryCode("SUB123");
        subsidiary.setCity("Timisoara");
        employee.setSubsidiary(subsidiary);

        return employee;
    }
}
