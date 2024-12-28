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
    public Organization addOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public boolean removeOrganization(Long registerCode) {
        if (organizationRepository.existsById(registerCode)) {
            organizationRepository.deleteById(registerCode);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Organization> updateOrganizationFields(Long registerCode, Organization updatedFields) {
        return organizationRepository.findById(registerCode).map(existingOrganization -> {
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
    public Optional<Organization> getOrganizationByRegisterCode(Long registerCode) {
        return organizationRepository.findById(registerCode);
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }
}
