package com.Server.service.interfaces;

import com.Server.repository.entity.Feedback;


public interface FeedbackService {
    void addFeedback(Long employeeId, Feedback feedback);
}
