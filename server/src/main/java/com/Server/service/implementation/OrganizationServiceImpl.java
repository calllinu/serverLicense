package com.Server.service.implementation;

import com.Server.repository.dto.OrganizationRequestDTO;
import com.Server.repository.dto.OrganizationResponseDTO;
import com.Server.repository.dto.UserResponseDTO;
import com.Server.repository.entity.Employee;
import com.Server.repository.entity.Organization;
import com.Server.repository.OrganizationRepository;
import com.Server.repository.entity.Role;
import com.Server.repository.entity.User;
import com.Server.service.OrganizationService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
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
    public boolean removeOrganization(Long organizationId) {
        if (organizationRepository.existsById(organizationId)) {
            organizationRepository.deleteById(organizationId);
            return true;
        }
        return false;
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
    public List<OrganizationResponseDTO> getAllOrganizations() {
        return organizationRepository.findAll().stream().map(organization -> {
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
        }).toList();
    }

}
