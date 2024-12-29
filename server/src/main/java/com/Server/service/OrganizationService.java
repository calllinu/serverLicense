package com.Server.service;

import com.Server.repository.entity.Organization;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {
    Organization addOrganization(Organization organization);
    boolean removeOrganization(Long organizationId);
    Optional<Organization> updateOrganizationFields(Long organizationId, Organization updatedFields);
    Optional<Organization> getOrganizationByRegisterCode(Long organizationId);
    List<Organization> getAllOrganizations();
}
