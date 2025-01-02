package com.Server.service.implementation;

import com.Server.repository.entity.Organization;
import com.Server.repository.OrganizationRepository;
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
    public void addOrganization(Organization organization) {
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
            if (updatedFields.getIndustry() != null) {
                existingOrganization.setIndustry(updatedFields.getIndustry());
            }
            return organizationRepository.save(existingOrganization);
        });
    }

    @Override
    public Optional<Organization> getOrganizationByRegisterCode(Long organizationId) {
        return organizationRepository.findById(organizationId);
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }
}
