package com.goniyo.notification.email;

import com.goniyo.notification.messagegenerator.MessageGenerator;
import com.goniyo.notification.notification.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class EmailServiceImpl implements EmailService {
    @Autowired
    private NotificationHandler<Email> notificationHandler;
    @Autowired
    MessageGenerator messageGenerator;
    @Autowired
    EmailNotifier<Email> emailNotifier;
    // add validation here
    public String sendEmail(EmailDto emailDto) {
        System.out.println("IN FACADE");
        Email email = new Email(); // TODO map SmsDTO()
        String message = messageGenerator.generateMessage("", email);
        //notificationHandler.setNotifierHandler(emailNotifier);
        return notificationHandler.sendNotification(emailNotifier, email);

    }
}
