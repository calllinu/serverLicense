package com.Server.repository;

import com.Server.repository.entity.Employee;
import com.Server.repository.entity.Subsidiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUserUserId(Long userId);
    void deleteByUserUserId(Long userId);
    @Query("SELECT e FROM Employee e WHERE e.subsidiary.subsidiaryId = :subsidiaryId")
    List<Employee> findAllBySubsidiaryId(@Param("subsidiaryId") Long subsidiaryId);
    Employee findByEmployeeId(Long employeeId);
}
