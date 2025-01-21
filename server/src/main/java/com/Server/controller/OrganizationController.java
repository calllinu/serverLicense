package com.Server.controller;

import com.Server.repository.dto.OrganizationRequestDTO;
import com.Server.repository.dto.OrganizationResponseDTO;
import com.Server.repository.entity.Organization;
import com.Server.service.OrganizationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<Void> removeOrganization(@PathVariable Long organizationId) {
        boolean isDeleted = organizationService.removeOrganization(organizationId);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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

    @GetMapping("/pageable-all")
    public ResponseEntity<Map<String, Object>> getAllOrganizations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") String search) {

        Page<OrganizationResponseDTO> organizations = organizationService.getAllOrganizationsPageable(PageRequest.of(page, size), search);

        Map<String, Object> response = new HashMap<>();
        response.put("data", organizations.getContent());
        response.put("total", organizations.getTotalElements());
        response.put("page", page);
        response.put("size", size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
    }


}
