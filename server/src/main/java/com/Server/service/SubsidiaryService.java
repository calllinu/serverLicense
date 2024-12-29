package com.Server.service;

import com.Server.repository.dto.SubsidiaryRequestDTO;
import com.Server.repository.dto.SubsidiaryResponseDTO;
import java.util.List;
import java.util.Optional;

public interface SubsidiaryService {

    SubsidiaryResponseDTO addSubsidiary(SubsidiaryRequestDTO subsidiaryRequestDTO);

    boolean removeSubsidiary(Long subsidiaryId);

    Optional<SubsidiaryResponseDTO> updateSubsidiaryFields(Long subsidiaryId, SubsidiaryRequestDTO updatedFields);

    Optional<SubsidiaryResponseDTO> getSubsidiaryById(Long subsidiaryId);

    List<SubsidiaryResponseDTO> getAllSubsidiaries();
}
