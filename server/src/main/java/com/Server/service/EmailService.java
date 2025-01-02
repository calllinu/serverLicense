package com.Server.service;

import com.Server.repository.entity.RegistrationRequest;
import com.Server.repository.entity.Subsidiary;
import com.Server.repository.entity.User;

import java.util.Map;

public interface EmailService {
    void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> templateData);
}
