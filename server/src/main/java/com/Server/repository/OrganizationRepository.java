package com.Server.repository;

import com.Server.repository.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    @Query("SELECT o FROM Organization o WHERE LOWER(o.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(o.organizationCode) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(o.industry) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Organization> findBySearch(@Param("search") String search, Pageable pageable);

    @Query("SELECT o from Organization o JOIN o.subsidiaries s JOIN s.employees e WHERE e.user.userId = :userId")
    Organization findByAdminUserId(@Param("userId") Long userId);
    Organization findByOrganizationCode(String organizationCode);
}
