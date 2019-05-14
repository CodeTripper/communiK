package com.goniyo.notification.email;

import com.goniyo.notification.messagegenerator.MessageGenerationException;
import com.goniyo.notification.messagegenerator.MessageGenerator;
import com.goniyo.notification.notification.NotificationHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class EmailServiceImpl implements EmailService {
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
        log.debug("generated email{}", message);
        Email email = Email.builder().message(message).subject(emailDto.getSubject()).build();
        return notificationHandler.sendNotification(emailNotifier, email);
    }
}
