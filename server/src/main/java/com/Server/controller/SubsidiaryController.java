package com.Server.controller;

import com.Server.exception.SubsidiaryNotFoundException;
import com.Server.repository.dto.SubsidiaryRequestDTO;
import com.Server.repository.dto.SubsidiaryResponseDTO;
import com.Server.repository.dto.SubsidiaryUpdateRequestDTO;
import com.Server.service.SubsidiaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subsidiaries")
public class SubsidiaryController {

    private final SubsidiaryService subsidiaryService;

    public SubsidiaryController(SubsidiaryService subsidiaryService) {
        this.subsidiaryService = subsidiaryService;
    }

    @PostMapping("/add")
    public ResponseEntity<SubsidiaryResponseDTO> addSubsidiary(@RequestBody SubsidiaryRequestDTO subsidiaryRequestDTO) {
        try {
            SubsidiaryResponseDTO createdSubsidiary = subsidiaryService.addSubsidiary(subsidiaryRequestDTO);
            return ResponseEntity.ok(createdSubsidiary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @DeleteMapping("/remove/{subsidiaryId}")
    public ResponseEntity<Void> removeSubsidiary(@PathVariable Long subsidiaryId) {
        try {
            subsidiaryService.removeSubsidiary(subsidiaryId);
            return ResponseEntity.ok().build();
        } catch (SubsidiaryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{subsidiaryId}")
    public ResponseEntity<Void> updateSubsidiaryFields(
            @PathVariable Long subsidiaryId,
            @RequestBody SubsidiaryUpdateRequestDTO updatedFields) {
        boolean isUpdated = subsidiaryService.updateSubsidiaryFields(subsidiaryId, updatedFields);
        return isUpdated ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/get/{subsidiaryId}")
    public ResponseEntity<SubsidiaryResponseDTO> getSubsidiaryById(@PathVariable Long subsidiaryId) {
        Optional<SubsidiaryResponseDTO> subsidiary = subsidiaryService.getSubsidiaryById(subsidiaryId);
        return subsidiary.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubsidiaryResponseDTO>> getAllSubsidiaries() {
        List<SubsidiaryResponseDTO> subsidiaries = subsidiaryService.getAllSubsidiaries();
        return ResponseEntity.ok(subsidiaries);
    }
}
