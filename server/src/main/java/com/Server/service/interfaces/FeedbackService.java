package com.Server.service.interfaces;

import com.Server.repository.entity.Feedback;

import java.util.List;
import java.util.Map;


public interface FeedbackService {
    void addFeedback(Long employeeId, Feedback feedback);
    Map<String, Object> getAllFeedbacks(int page, int size);
    List<Feedback> getFeedbacksForOrganization(String organizationCode);
}
