package com.Server.controller;

import com.Server.repository.entity.Feedback;
import com.Server.service.interfaces.FeedbackService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/add/{employeeId}")
    public void addFeedback(@PathVariable Long employeeId, @RequestBody Feedback feedback) {
        feedbackService.addFeedback(employeeId, feedback);
    }
}
