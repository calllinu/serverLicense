package com.Server.service.implementation;

import com.Server.exception.SubsidiaryNotFoundException;
import com.Server.repository.EmployeeRepository;
import com.Server.repository.SubsidiaryRepository;
import com.Server.repository.OrganizationRepository;
import com.Server.repository.UserRepository;
import com.Server.repository.dto.subsidiaryDTOs.SubsidiaryUpdateRequestDTO;
import com.Server.repository.entity.Employee;
import com.Server.repository.entity.Organization;
import com.Server.repository.entity.Subsidiary;
import com.Server.repository.dto.subsidiaryDTOs.SubsidiaryRequestDTO;
import com.Server.repository.dto.subsidiaryDTOs.SubsidiaryResponseDTO;
import com.Server.repository.entity.User;
import com.Server.service.interfaces.SubsidiaryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubsidiaryServiceImpl implements SubsidiaryService {

    private final SubsidiaryRepository subsidiaryRepository;
    private final OrganizationRepository organizationRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public SubsidiaryServiceImpl(SubsidiaryRepository subsidiaryRepository,
                                 OrganizationRepository organizationRepository,
                                 EmployeeRepository employeeRepository,
                                 UserRepository userRepository) {
        this.subsidiaryRepository = subsidiaryRepository;
        this.organizationRepository = organizationRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
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
    public void removeSubsidiary(Long subsidiaryId) {
        Subsidiary subsidiary = subsidiaryRepository.findById(subsidiaryId)
                .orElseThrow(() -> new SubsidiaryNotFoundException("Subsidiary with ID " + subsidiaryId + " not found."));

        for (Employee employee : subsidiary.getEmployees()) {
            User user = employee.getUser();
            if (user != null) {
                userRepository.delete(user);
            }
            employeeRepository.delete(employee);
        }
        subsidiaryRepository.delete(subsidiary);
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
