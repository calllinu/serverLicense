package com.Server.service.implementation;

import com.Server.service.EmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;

@Service
@Log4j2
@Transactional
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final Configuration configuration;

    public EmailServiceImpl(JavaMailSender mailSender, Configuration configuration) {
        this.mailSender = mailSender;
        this.configuration = configuration;
    }

    @Override
    public void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> templateData) {
        try {
            Template template = configuration.getTemplate(templateName);
            String body = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateData);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            log.info("Email sent to {} with subject: {}", to, subject);
        } catch (IOException | TemplateException | MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
