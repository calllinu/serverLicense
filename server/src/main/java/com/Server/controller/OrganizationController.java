package com.Server.controller;

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
    public ResponseEntity<Organization> addOrganization(@RequestBody Organization organization) {
        Organization createdOrganization = organizationService.addOrganization(organization);
        return ResponseEntity.ok(createdOrganization);
    }

    @DeleteMapping("/remove/{registerCode}")
    public ResponseEntity<String> removeOrganization(@PathVariable Long registerCode) {
        boolean isDeleted = organizationService.removeOrganization(registerCode);
        if (isDeleted) {
            return ResponseEntity.ok("Organization removed successfully.");
        } else {
            return ResponseEntity.status(404).body("Organization not found.");
        }
    }

    @PutMapping("/update/{registerCode}")
    public ResponseEntity<Organization> updateOrganizationFields(
            @PathVariable Long registerCode,
            @RequestBody Organization updatedFields) {
        Optional<Organization> updatedOrganization = organizationService.updateOrganizationFields(registerCode, updatedFields);
        return updatedOrganization.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @GetMapping("/get/{registerCode}")
    public ResponseEntity<Organization> getOrganizationByRegisterCode(@PathVariable Long registerCode) {
        Optional<Organization> organization = organizationService.getOrganizationByRegisterCode(registerCode);
        return organization.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
    }
}
