package com.Server.service.implementation;

import com.Server.repository.SubsidiaryRepository;
import com.Server.repository.OrganizationRepository;
import com.Server.repository.dto.SubsidiaryUpdateRequestDTO;
import com.Server.repository.entity.Organization;
import com.Server.repository.entity.Subsidiary;
import com.Server.repository.dto.SubsidiaryRequestDTO;
import com.Server.repository.dto.SubsidiaryResponseDTO;
import com.Server.service.SubsidiaryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class SubsidiaryServiceImpl implements SubsidiaryService {

    private final SubsidiaryRepository subsidiaryRepository;
    private final OrganizationRepository organizationRepository;

    public SubsidiaryServiceImpl(SubsidiaryRepository subsidiaryRepository, OrganizationRepository organizationRepository) {
        this.subsidiaryRepository = subsidiaryRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public SubsidiaryResponseDTO addSubsidiary(SubsidiaryRequestDTO subsidiaryRequestDTO) {

        Optional<Organization> organizationOptional = organizationRepository.findById(subsidiaryRequestDTO.getOrganizationId());

        if (organizationOptional.isEmpty()) {
            throw new IllegalArgumentException("Organization with ID " + subsidiaryRequestDTO.getOrganizationId() + " not found.");
        }

        Organization organization = organizationOptional.get();

        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setSubsidiaryCode(subsidiaryRequestDTO.getSubsidiaryCode());
        subsidiary.setCountry(subsidiaryRequestDTO.getCountry());
        subsidiary.setCity(subsidiaryRequestDTO.getCity());
        subsidiary.setAddress(subsidiaryRequestDTO.getAddress());
        subsidiary.setOrganization(organization);

        Subsidiary savedSubsidiary = subsidiaryRepository.save(subsidiary);

        return mapToSubsidiaryResponseDTO(savedSubsidiary);
    }

    @Override
    public boolean removeSubsidiary(Long subsidiaryId) {
        if (subsidiaryRepository.existsById(subsidiaryId)) {
            subsidiaryRepository.deleteById(subsidiaryId);
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateSubsidiaryFields(Long subsidiaryId, SubsidiaryUpdateRequestDTO updatedFields) {
        return subsidiaryRepository.findById(subsidiaryId)
                .map(existingSubsidiary -> {
                    updateIfNotNull(existingSubsidiary::setCountry, updatedFields.getCountry());
                    updateIfNotNull(existingSubsidiary::setCity, updatedFields.getCity());
                    updateIfNotNull(existingSubsidiary::setAddress, updatedFields.getAddress());

                    subsidiaryRepository.save(existingSubsidiary);
                    return true;
                }).orElse(false);
    }

    private <T> void updateIfNotNull(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }

    @Override
    public Optional<SubsidiaryResponseDTO> getSubsidiaryById(Long subsidiaryId) {
        return subsidiaryRepository.findById(subsidiaryId).map(this::mapToSubsidiaryResponseDTO);
    }

    @Override
    public List<SubsidiaryResponseDTO> getAllSubsidiaries() {
        return subsidiaryRepository.findAll().stream()
                .map(this::mapToSubsidiaryResponseDTO)
                .collect(Collectors.toList());
    }

    private SubsidiaryResponseDTO mapToSubsidiaryResponseDTO(Subsidiary subsidiary) {
        SubsidiaryResponseDTO responseDTO = new SubsidiaryResponseDTO();
        responseDTO.setSubsidiaryId(subsidiary.getSubsidiaryId());
        responseDTO.setSubsidiaryCode(subsidiary.getSubsidiaryCode());
        responseDTO.setCountry(subsidiary.getCountry());
        responseDTO.setCity(subsidiary.getCity());
        responseDTO.setAddress(subsidiary.getAddress());
        responseDTO.setOrganizationId(subsidiary.getOrganization().getOrganizationId());
        return responseDTO;
    }
}
