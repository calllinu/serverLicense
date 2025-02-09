package com.Server.service.implementation;

import com.Server.exception.OrganizationNotFoundException;
import com.Server.repository.dto.OrganizationRequestDTO;
import com.Server.repository.dto.OrganizationResponseDTO;
import com.Server.repository.SubsidiaryRepository;
import com.Server.repository.EmployeeRepository;
import com.Server.repository.RegistrationRequestRepository;
import com.Server.repository.dto.SubsidiaryForOrganizationDTO;
import com.Server.repository.dto.UserResponseDTO;
import com.Server.repository.entity.*;
import com.Server.repository.OrganizationRepository;
import com.Server.service.OrganizationService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final SubsidiaryRepository subsidiaryRepository;
    private final EmployeeRepository employeeRepository;
    private final RegistrationRequestRepository registrationRequestRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   SubsidiaryRepository subsidiaryRepository,
                                   EmployeeRepository employeeRepository,
                                   RegistrationRequestRepository registrationRequestRepository) {
        this.organizationRepository = organizationRepository;
        this.subsidiaryRepository = subsidiaryRepository;
        this.employeeRepository = employeeRepository;
        this.registrationRequestRepository = registrationRequestRepository;
    }

    @Override
    public void addOrganization(OrganizationRequestDTO organizationRequestDTO) {
        Organization organization = new Organization();
        organization.setOrganizationCode(organizationRequestDTO.getOrganizationCode());
        organization.setName(organizationRequestDTO.getName());
        organization.setYearOfEstablishment(organizationRequestDTO.getYearOfEstablishment());
        organization.setIndustry(organizationRequestDTO.getIndustry());
        organizationRepository.save(organization);
    }

    @Override
    public void deleteOrganization(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found"));

        for (Subsidiary subsidiary : organization.getSubsidiaries()) {
            registrationRequestRepository.deleteAll(subsidiary.getRegistrationRequests());
        }

        for (Subsidiary subsidiary : organization.getSubsidiaries()) {
            employeeRepository.deleteAll(subsidiary.getEmployees());
        }

        subsidiaryRepository.deleteAll(organization.getSubsidiaries());

        organizationRepository.delete(organization);
    }

    @Override
    public Optional<Organization> updateOrganizationFields(Long organizationId, Organization updatedFields) {
        return organizationRepository.findById(organizationId).map(existingOrganization -> {
            if (updatedFields.getName() != null) {
                existingOrganization.setName(updatedFields.getName());
            }
            if (updatedFields.getYearOfEstablishment() != null) {
                existingOrganization.setYearOfEstablishment(updatedFields.getYearOfEstablishment());
            }
            return organizationRepository.save(existingOrganization);
        });
    }

    @Override
    public Optional<Organization> getOrganizationByRegisterCode(Long organizationId) {
        return organizationRepository.findById(organizationId);
    }

    @Override
    public Page<OrganizationResponseDTO> getAllOrganizationsPageable(PageRequest pageRequest, String search) {
        return organizationRepository.findBySearch(search, pageRequest).map(organization -> {
            OrganizationResponseDTO dto = new OrganizationResponseDTO();
            dto.setOrganizationId(organization.getOrganizationId());
            dto.setOrganizationCode(organization.getOrganizationCode());
            dto.setName(organization.getName());
            dto.setYearOfEstablishment(organization.getYearOfEstablishment());
            dto.setIndustry(organization.getIndustry());
            dto.setSubsidiaries(organization.getSubsidiaries());

            User orgAdmin = organization.getSubsidiaries().stream()
                    .flatMap(subsidiary -> subsidiary.getEmployees().stream())
                    .map(Employee::getUser)
                    .filter(user -> user.getRole() == Role.ORG_ADMIN)
                    .findFirst()
                    .orElse(null);

            if (orgAdmin != null) {
                UserResponseDTO orgAdminDTO = new UserResponseDTO();
                orgAdminDTO.setUsername(orgAdmin.getUsername());
                orgAdminDTO.setFullName(orgAdmin.getFullName());
                orgAdminDTO.setEmail(orgAdmin.getEmail());
                orgAdminDTO.setRole(orgAdmin.getRole());

                dto.setAdmin(orgAdminDTO);
            }

            return dto;
        });
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    @Override
    public SubsidiaryForOrganizationDTO getAllSubsidiariesForOrganization(Long userId){
        Organization organization = organizationRepository.findByAdminUserId(userId);
        Long organizationId = organization.getOrganizationId();
        List<Subsidiary> subsidiaries = organization.getSubsidiaries();

        return new SubsidiaryForOrganizationDTO(organizationId, subsidiaries);
    }


}
