package com.nvtt.services.impl;

import com.nvtt.services.email.EmailService;
import com.nvtt.utils.constants.EmailType;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LogManager.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    @Override
    public void sendAccountNotification(String to, String name, EmailType type) {
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(getSubject(type));

            Context thymeleafContext = new Context();
            thymeleafContext.setVariable("recipientName", name);
            thymeleafContext.setVariable("emailTitle", getTitle(type));
            thymeleafContext.setVariable("emailMessage", getMessage(type));

            String htmlContent = templateEngine.process("mail/account-notification", thymeleafContext);

            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            logger.info("Async email sent successfully to: {} with type: {}", to, type);

        } catch (Exception e) {
            logger.error("Failed to send asynchronous email notification to {} due to error: {}", to, e.getMessage());
        }
    }

    private String getSubject(EmailType type) {

        return switch (type) {

            case ORGANIZER_APPROVED ->
                "Organizer Account Approved";

            case ACCOUNT_LOCKED ->
                "Account Locked";

            case ACCOUNT_UNLOCKED ->
                "Account Unlocked";
        };
    }

    private String getTitle(EmailType type) {

        return switch (type) {

            case ORGANIZER_APPROVED ->
                "Organizer Approved";

            case ACCOUNT_LOCKED ->
                "Account Locked";

            case ACCOUNT_UNLOCKED ->
                "Account Unlocked";
        };
    }

    private String getMessage(EmailType type) {

        return switch (type) {

            case ORGANIZER_APPROVED ->
                "Your organizer account has been approved successfully.";

            case ACCOUNT_LOCKED ->
                "Your account has been temporarily locked by the administrator.";

            case ACCOUNT_UNLOCKED ->
                "Your account has been unlocked and reactivated.";
        };
    }

}
