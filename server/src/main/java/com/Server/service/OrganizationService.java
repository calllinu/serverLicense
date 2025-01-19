package com.Server.service;

import com.Server.repository.dto.OrganizationRequestDTO;
import com.Server.repository.dto.OrganizationResponseDTO;
import com.Server.repository.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {
    void addOrganization(OrganizationRequestDTO organizationRequestDTO);
    boolean removeOrganization(Long organizationId);
    Optional<Organization> updateOrganizationFields(Long organizationId, Organization updatedFields);
    Optional<Organization> getOrganizationByRegisterCode(Long organizationId);
    Page<OrganizationResponseDTO> getAllOrganizations(PageRequest pageRequest);
}
