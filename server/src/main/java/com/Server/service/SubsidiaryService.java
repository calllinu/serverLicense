package com.Server.service;

import com.Server.repository.dto.SubsidiaryRequestDTO;
import com.Server.repository.dto.SubsidiaryResponseDTO;
import com.Server.repository.dto.SubsidiaryUpdateRequestDTO;

import java.util.List;
import java.util.Optional;

public interface SubsidiaryService {

    SubsidiaryResponseDTO addSubsidiary(SubsidiaryRequestDTO subsidiaryRequestDTO);

    boolean removeSubsidiary(Long subsidiaryId);

    Boolean updateSubsidiaryFields(Long subsidiaryId, SubsidiaryUpdateRequestDTO updatedFields);

    Optional<SubsidiaryResponseDTO> getSubsidiaryById(Long subsidiaryId);

    List<SubsidiaryResponseDTO> getAllSubsidiaries();
}
