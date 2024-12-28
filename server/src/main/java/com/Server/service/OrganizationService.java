package com.Server.service;

import com.Server.repository.entity.Organization;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {
    Organization addOrganization(Organization organization);
    boolean removeOrganization(Long registerCode);
    Optional<Organization> updateOrganizationFields(Long registerCode, Organization updatedFields);
    Optional<Organization> getOrganizationByRegisterCode(Long registerCode);
    List<Organization> getAllOrganizations();
}
