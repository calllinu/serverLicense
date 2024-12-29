package com.Server.controller;

import com.Server.repository.dto.SubsidiaryRequestDTO;
import com.Server.repository.dto.SubsidiaryResponseDTO;
import com.Server.service.SubsidiaryService;
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

    // Create a new subsidiary
    @PostMapping("/add")
    public ResponseEntity<SubsidiaryResponseDTO> addSubsidiary(@RequestBody SubsidiaryRequestDTO subsidiaryRequestDTO) {
        try {
            SubsidiaryResponseDTO createdSubsidiary = subsidiaryService.addSubsidiary(subsidiaryRequestDTO);
            return ResponseEntity.ok(createdSubsidiary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null); // Organization not found
        }
    }

    // Delete a subsidiary by ID
    @DeleteMapping("/remove/{subsidiaryId}")
    public ResponseEntity<String> removeSubsidiary(@PathVariable Long subsidiaryId) {
        boolean isDeleted = subsidiaryService.removeSubsidiary(subsidiaryId);
        if (isDeleted) {
            return ResponseEntity.ok("Subsidiary removed successfully.");
        } else {
            return ResponseEntity.status(404).body("Subsidiary not found.");
        }
    }

    // Update subsidiary fields
    @PutMapping("/update/{subsidiaryId}")
    public ResponseEntity<SubsidiaryResponseDTO> updateSubsidiaryFields(
            @PathVariable Long subsidiaryId,
            @RequestBody SubsidiaryRequestDTO updatedFields) {
        Optional<SubsidiaryResponseDTO> updatedSubsidiary = subsidiaryService.updateSubsidiaryFields(subsidiaryId, updatedFields);
        return updatedSubsidiary.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    // Get a subsidiary by its ID
    @GetMapping("/get/{subsidiaryId}")
    public ResponseEntity<SubsidiaryResponseDTO> getSubsidiaryById(@PathVariable Long subsidiaryId) {
        Optional<SubsidiaryResponseDTO> subsidiary = subsidiaryService.getSubsidiaryById(subsidiaryId);
        return subsidiary.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    // Get all subsidiaries
    @GetMapping("/all")
    public ResponseEntity<List<SubsidiaryResponseDTO>> getAllSubsidiaries() {
        List<SubsidiaryResponseDTO> subsidiaries = subsidiaryService.getAllSubsidiaries();
        return ResponseEntity.ok(subsidiaries);
    }
}
