package com.Server.controller;

import com.Server.repository.dto.OrganizationRequestDTO;
import com.Server.repository.dto.OrganizationResponseDTO;
import com.Server.repository.entity.Organization;
import com.Server.service.OrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addOrganization(@RequestBody OrganizationRequestDTO organizationRequestDTO) {
        organizationService.addOrganization(organizationRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{organizationId}")
    public ResponseEntity<String> removeOrganization(@PathVariable Long organizationId) {
        boolean isDeleted = organizationService.removeOrganization(organizationId);
        if (isDeleted) {
            return ResponseEntity.ok("Organization removed successfully.");
        } else {
            return ResponseEntity.status(404).body("Organization not found.");
        }
    }

    @PutMapping("/update/{organizationId}")
    public ResponseEntity<Organization> updateOrganizationFields(
            @PathVariable Long organizationId,
            @RequestBody Organization updatedFields) {
        Optional<Organization> updatedOrganization = organizationService.updateOrganizationFields(organizationId, updatedFields);
        return updatedOrganization.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @GetMapping("/get/{organizationId}")
    public ResponseEntity<Organization> getOrganizationByRegisterCode(@PathVariable Long organizationId) {
        Optional<Organization> organization = organizationService.getOrganizationByRegisterCode(organizationId);
        return organization.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrganizationResponseDTO>> getAllOrganizations() {
        List<OrganizationResponseDTO> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
    }
}
