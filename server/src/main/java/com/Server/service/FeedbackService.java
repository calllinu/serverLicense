package com.Server.service;

import com.Server.repository.entity.Feedback;

public interface FeedbackService {
    void addFeedback(Long employeeId, Feedback feedback);
}
