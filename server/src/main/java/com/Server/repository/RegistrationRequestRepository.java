package com.Server.repository;

import com.Server.repository.entity.Organization;
import com.Server.repository.entity.RegistrationRequest;
import com.Server.repository.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long> {
    //List<RegistrationRequest> findByOrganizationAndStatus(Organization organization, RequestStatus status);
}

