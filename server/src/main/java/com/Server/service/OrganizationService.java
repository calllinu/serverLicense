package com.Server.service;

import com.Server.repository.dto.OrganizationRequestDTO;
import com.Server.repository.dto.OrganizationResponseDTO;
import com.Server.repository.entity.Organization;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {
    void addOrganization(OrganizationRequestDTO organizationRequestDTO);
    boolean removeOrganization(Long organizationId);
    Optional<Organization> updateOrganizationFields(Long organizationId, Organization updatedFields);
    Optional<Organization> getOrganizationByRegisterCode(Long organizationId);
    List<OrganizationResponseDTO> getAllOrganizations();
}
