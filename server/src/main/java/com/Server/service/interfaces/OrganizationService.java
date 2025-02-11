package com.Server.service.interfaces;

import com.Server.repository.dto.organizationDTOs.OrganizationRequestDTO;
import com.Server.repository.dto.organizationDTOs.OrganizationResponseDTO;
import com.Server.repository.dto.subsidiaryDTOs.SubsidiaryForOrganizationDTO;
import com.Server.repository.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {
    void addOrganization(OrganizationRequestDTO organizationRequestDTO);
    void deleteOrganization(Long organizationId);
    Optional<Organization> updateOrganizationFields(Long organizationId, Organization updatedFields);
    Optional<Organization> getOrganizationByRegisterCode(Long organizationId);
    Page<OrganizationResponseDTO> getAllOrganizationsPageable(PageRequest pageRequest, String search);
    List<Organization> getAllOrganizations();
    SubsidiaryForOrganizationDTO getAllSubsidiariesForOrganization(Long userId);
}
