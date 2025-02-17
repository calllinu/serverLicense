package com.Server.controller;

import com.Server.repository.entity.Feedback;
import com.Server.service.interfaces.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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

    @GetMapping("/pageable-all")
    public ResponseEntity<Map<String, Object>> getAllFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> response = feedbackService.getAllFeedbacks(page, size);
        return ResponseEntity.ok(response);
    }
}
