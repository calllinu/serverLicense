package com.Server.service.interfaces;

import java.util.Map;

public interface EmailService {
    void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> templateData);
}
