package com.Server.Tests;

import com.Server.exception.OrganizationNotFoundException;
import com.Server.repository.*;
import com.Server.repository.dto.organizationDTOs.OrganizationRequestDTO;
import com.Server.repository.dto.organizationDTOs.OrganizationResponseDTO;
import com.Server.repository.dto.subsidiaryDTOs.SubsidiaryForOrganizationDTO;
import com.Server.repository.dto.userDTOs.UserResponseDTO;
import com.Server.repository.entity.*;
import com.Server.repository.entity.enums.Industry;
import com.Server.repository.entity.enums.Role;
import com.Server.service.implementation.OrganizationServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizationServiceImplTests {

    @Mock
    private OrganizationRepository organizationRepo;

    @Mock
    private SubsidiaryRepository subsidiaryRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private RegistrationRequestRepository registrationRequestRepo;

    @InjectMocks
    private OrganizationServiceImpl organizationService;

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
    void shouldAddOrganization() {
        OrganizationRequestDTO request = new OrganizationRequestDTO();
        request.setOrganizationCode("ORG001");
        request.setName("Organization");
        request.setYearOfEstablishment(1999L);
        request.setIndustry(Industry.AGRICULTURE);

        organizationService.addOrganization(request);

        ArgumentCaptor<Organization> captor = ArgumentCaptor.forClass(Organization.class);
        verify(organizationRepo).save(captor.capture());

        Organization saved = captor.getValue();
        assertEquals("ORG001", saved.getOrganizationCode());
        assertEquals("Organization", saved.getName());
        assertEquals(1999, saved.getYearOfEstablishment());
        assertEquals(Industry.AGRICULTURE, saved.getIndustry());
    }

    @Test
    void shouldDeleteOrganizationWithRelations() {
        Organization org = new Organization();
        Subsidiary sub1 = new Subsidiary();
        sub1.setEmployees(List.of(new Employee()));
        sub1.setRegistrationRequests(List.of(new RegistrationRequest()));
        org.setSubsidiaries(List.of(sub1));

        when(organizationRepo.findById(1L)).thenReturn(Optional.of(org));

        organizationService.deleteOrganization(1L);

        verify(registrationRequestRepo).deleteAll(sub1.getRegistrationRequests());
        verify(employeeRepo).deleteAll(sub1.getEmployees());
        verify(subsidiaryRepo).deleteAll(org.getSubsidiaries());
        verify(organizationRepo).delete(org);
    }

    @Test
    void shouldThrowException_whenOrganizationNotFoundForDelete() {
        when(organizationRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(OrganizationNotFoundException.class, () -> organizationService.deleteOrganization(1L));
    }

    @Test
    void shouldUpdateOrganizationFields() {
        Organization existing = new Organization();
        existing.setName("Name");

        Organization updated = new Organization();
        updated.setName("Name1");
        updated.setYearOfEstablishment(2000L);

        when(organizationRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(organizationRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Optional<Organization> result = organizationService.updateOrganizationFields(1L, updated);

        assertTrue(result.isPresent());
        assertEquals("Name1", result.get().getName());
        assertEquals(2000, result.get().getYearOfEstablishment());
    }

    @Test
    void shouldReturnOrganizationById() {
        Organization org = new Organization();
        when(organizationRepo.findById(1L)).thenReturn(Optional.of(org));

        Optional<Organization> result = organizationService.getOrganizationByRegisterCode(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnPagedOrganizationsWithAdmin() {
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setFullName("Admin User");
        adminUser.setEmail("admin@gmail.com");
        adminUser.setRole(Role.ORG_ADMIN);

        Employee adminEmployee = new Employee();
        adminEmployee.setUser(adminUser);

        Subsidiary sub = new Subsidiary();
        sub.setEmployees(List.of(adminEmployee));

        Organization org = new Organization();
        org.setOrganizationId(1L);
        org.setOrganizationCode("ORG001");
        org.setName("TestOrg");
        org.setYearOfEstablishment(1999L);
        org.setIndustry(Industry.AGRICULTURE);
        org.setSubsidiaries(List.of(sub));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Organization> page = new PageImpl<>(List.of(org));

        when(organizationRepo.findBySearch("Test", pageRequest)).thenReturn(page);

        Page<OrganizationResponseDTO> result = organizationService.getAllOrganizationsPageable(pageRequest, "Test");

        assertEquals(1, result.getContent().size());
        OrganizationResponseDTO dto = result.getContent().get(0);
        assertEquals("TestOrg", dto.getName());

        UserResponseDTO adminDto = dto.getAdmin();
        assertNotNull(adminDto);
        assertEquals("admin", adminDto.getUsername());
    }

    @Test
    void shouldReturnAllOrganizations() {
        List<Organization> allOrgs = List.of(new Organization(), new Organization());
        when(organizationRepo.findAll()).thenReturn(allOrgs);

        List<Organization> result = organizationService.getAllOrganizations();
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnAllSubsidiariesForOrganization() {
        Organization org = new Organization();
        org.setOrganizationId(10L);

        Subsidiary sub1 = new Subsidiary();
        Subsidiary sub2 = new Subsidiary();
        org.setSubsidiaries(List.of(sub1, sub2));

        when(organizationRepo.findByAdminUserId(5L)).thenReturn(org);

        SubsidiaryForOrganizationDTO result = organizationService.getAllSubsidiariesForOrganization(5L);

        assertEquals(10L, result.getOrganizationId());
        assertEquals(2, result.getSubsidiaries().size());
    }

    @Test
    void shouldReturnAllOrganizationCodes() {
        Organization org1 = new Organization();
        org1.setOrganizationCode("ORG001");
        Organization org2 = new Organization();
        org2.setOrganizationCode("ORG002");

        when(organizationRepo.findAll()).thenReturn(List.of(org1, org2));

        List<String> result = organizationService.getAllOrganizationCodes();
        assertEquals(List.of("ORG001", "ORG002"), result);
    }
}
