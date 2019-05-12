package com.goniyo.notification.email;

import com.goniyo.notification.messagegenerator.MessageGenerator;
import com.goniyo.notification.notification.NotificationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class SalaryCreditedEmailService implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(SalaryCreditedEmailService.class);
    @Autowired
    private NotificationHandler<Email> notificationHandler;
    @Autowired
    MessageGenerator messageGenerator;
    @Autowired
    EmailNotifier<Email> emailNotifier;

    // add validation here
    public String sendEmail(EmailDto emailDto) {
        Email email = null;//ImmutableEmailBuilder().builder.to("").build(); // TODO copy only req data to Email
        String message = messageGenerator.generateMessage("email/salary-credited.ftl", emailDto);
        logger.debug("generated email{}", message);
        return notificationHandler.sendNotification(emailNotifier, email);

    }

    public String sendSalaryEmail(EmailDto emailDto) {
        Email email = null;//ImmutableEmailBuilder().builder.to("").build(); // TODO copy only req data to Email
        String message = messageGenerator.generateMessage("email/salary-credited.ftl", emailDto);
        logger.debug("generated email{}", message);
        return notificationHandler.sendNotification(emailNotifier, email);

    }
}
