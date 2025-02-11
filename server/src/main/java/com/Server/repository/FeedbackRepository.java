package com.Server.repository;

import com.Server.repository.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    boolean existsByEmployeeUserUserId(Long employeeId);
}



