package com.Server.Tests;

import com.Server.exception.SubsidiaryNotFoundException;
import com.Server.repository.*;
import com.Server.repository.dto.subsidiaryDTOs.SubsidiaryRequestDTO;
import com.Server.repository.dto.subsidiaryDTOs.SubsidiaryResponseDTO;
import com.Server.repository.dto.subsidiaryDTOs.SubsidiaryUpdateRequestDTO;
import com.Server.repository.entity.*;
import com.Server.service.implementation.SubsidiaryServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubsidiaryServiceImplTests {

    @Mock
    private SubsidiaryRepository subsidiaryRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SubsidiaryServiceImpl subsidiaryService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    void addSubsidiary_shouldReturnDTO_whenOrganizationExists() {
        SubsidiaryRequestDTO request = new SubsidiaryRequestDTO();
        request.setOrganizationId(1L);
        request.setSubsidiaryCode("SUB001");
        request.setCountry("Country");
        request.setCity("City");
        request.setAddress("Address");

        Organization org = new Organization();
        org.setOrganizationId(1L);

        when(organizationRepository.findById(1L)).thenReturn(Optional.of(org));
        when(subsidiaryRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        SubsidiaryResponseDTO response = subsidiaryService.addSubsidiary(request);

        assertEquals("SUB001", response.getSubsidiaryCode());
        assertEquals("Country", response.getCountry());
        assertEquals(1L, response.getOrganizationId());
    }

    @Test
    void addSubsidiary_shouldThrowException_whenOrganizationNotFound() {
        SubsidiaryRequestDTO request = new SubsidiaryRequestDTO();
        request.setOrganizationId(99L);

        when(organizationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> subsidiaryService.addSubsidiary(request));
    }

    @Test
    void removeSubsidiary_shouldDeleteAllRelatedEntities() {
        User user = new User();
        Employee emp = new Employee();
        emp.setUser(user);

        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setEmployees(List.of(emp));

        when(subsidiaryRepository.findById(5L)).thenReturn(Optional.of(subsidiary));

        subsidiaryService.removeSubsidiary(5L);

        verify(userRepository).delete(user);
        verify(employeeRepository).delete(emp);
        verify(subsidiaryRepository).delete(subsidiary);
    }

    @Test
    void removeSubsidiary_shouldThrowException_whenSubsidiaryNotFound() {
        when(subsidiaryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SubsidiaryNotFoundException.class, () -> subsidiaryService.removeSubsidiary(1L));
    }

    @Test
    void updateSubsidiaryFields_shouldUpdateOnlyProvidedFields() {
        Subsidiary existing = new Subsidiary();
        existing.setCountry("OldCountry");
        existing.setCity("OldCity");
        existing.setAddress("OldAddress");

        SubsidiaryUpdateRequestDTO update = new SubsidiaryUpdateRequestDTO();
        update.setCity("NewCity"); // Only city is updated

        when(subsidiaryRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(subsidiaryRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Boolean updated = subsidiaryService.updateSubsidiaryFields(10L, update);

        assertTrue(updated);
        assertEquals("NewCity", existing.getCity());
        assertEquals("OldCountry", existing.getCountry());
    }

    @Test
    void updateSubsidiaryFields_shouldReturnFalse_whenSubsidiaryNotFound() {
        when(subsidiaryRepository.findById(10L)).thenReturn(Optional.empty());

        SubsidiaryUpdateRequestDTO update = new SubsidiaryUpdateRequestDTO();
        update.setCity("New");

        Boolean result = subsidiaryService.updateSubsidiaryFields(10L, update);
        assertFalse(result);
    }

    @Test
    void getSubsidiaryById_shouldReturnDTO_ifExists() {
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setSubsidiaryId(1L);
        subsidiary.setSubsidiaryCode("CODE123");
        Organization org = new Organization();
        org.setOrganizationId(9L);
        subsidiary.setOrganization(org);

        when(subsidiaryRepository.findById(1L)).thenReturn(Optional.of(subsidiary));

        Optional<SubsidiaryResponseDTO> result = subsidiaryService.getSubsidiaryById(1L);

        assertTrue(result.isPresent());
        assertEquals("CODE123", result.get().getSubsidiaryCode());
        assertEquals(9L, result.get().getOrganizationId());
    }

    @Test
    void getAllSubsidiaries_shouldReturnListOfDTOs() {
        Subsidiary sub1 = new Subsidiary();
        sub1.setSubsidiaryCode("S1");
        sub1.setOrganization(new Organization());

        Subsidiary sub2 = new Subsidiary();
        sub2.setSubsidiaryCode("S2");
        sub2.setOrganization(new Organization());

        when(subsidiaryRepository.findAll()).thenReturn(List.of(sub1, sub2));

        List<SubsidiaryResponseDTO> result = subsidiaryService.getAllSubsidiaries();

        assertEquals(2, result.size());
        assertEquals("S1", result.get(0).getSubsidiaryCode());
        assertEquals("S2", result.get(1).getSubsidiaryCode());
    }
}
