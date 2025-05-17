package com.Server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/prediction-data")
public class PredictionController {

    @PostMapping("/predict")
    public ResponseEntity<?> predict(@RequestBody Map<String, Object> payload) {
        RestTemplate restTemplate = new RestTemplate();
        String flaskUrl = "http://localhost:5000/predict";
        ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, payload, Map.class);

        return ResponseEntity.ok(response.getBody());
    }
}