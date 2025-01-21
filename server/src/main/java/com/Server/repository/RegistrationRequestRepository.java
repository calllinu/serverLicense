package com.Server.repository;

import com.Server.repository.entity.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long> {
    List<RegistrationRequest> findByAdminId(Long adminId);
}
