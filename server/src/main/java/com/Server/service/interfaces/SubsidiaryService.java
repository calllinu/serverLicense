package com.Server.service.interfaces;

import com.Server.repository.dto.subsidiaryDTOs.SubsidiaryRequestDTO;
import com.Server.repository.dto.subsidiaryDTOs.SubsidiaryResponseDTO;
import com.Server.repository.dto.subsidiaryDTOs.SubsidiaryUpdateRequestDTO;

import java.util.List;
import java.util.Optional;

public interface SubsidiaryService {

    SubsidiaryResponseDTO addSubsidiary(SubsidiaryRequestDTO subsidiaryRequestDTO);

    void removeSubsidiary(Long subsidiaryId);

    Boolean updateSubsidiaryFields(Long subsidiaryId, SubsidiaryUpdateRequestDTO updatedFields);

    Optional<SubsidiaryResponseDTO> getSubsidiaryById(Long subsidiaryId);

    List<SubsidiaryResponseDTO> getAllSubsidiaries();
}
