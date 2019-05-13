package com.goniyo.notification.email;

import com.goniyo.notification.messagegenerator.MessageGenerationException;
import com.goniyo.notification.messagegenerator.MessageGenerator;
import com.goniyo.notification.notification.NotificationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    @Autowired
    private NotificationHandler<Email> notificationHandler;
    @Autowired
    private MessageGenerator messageGenerator;
    @Autowired
    private EmailNotifier<Email> emailNotifier;

    // TODO add validation here
    public String salaryCredited(EmailDto emailDto) {
        return sendEmail(emailDto, "email/salary-credited.ftl");

    }

    @Override
    public String salaryAdvanceCredited(EmailDto emailDto) {
        return sendEmail(emailDto, "email/salary-advance-credited.ftl");
    }

    @Override
    public String promoMailToCustomers(EmailDto emailDto) {
        return sendEmail(emailDto, "templates/email/promo-mail-to-customers.ftl");
    }

    private String sendEmail(EmailDto emailDto, String s) {
        String message = null;
        try {
            message = messageGenerator.generateMessage(s, emailDto);
        } catch (MessageGenerationException e) {
            e.printStackTrace();
        }
        logger.debug("generated email{}", message);
        Email email = new Email();
        email.setId(UUID.randomUUID().toString());
        return notificationHandler.sendNotification(emailNotifier, email);
    }
}
