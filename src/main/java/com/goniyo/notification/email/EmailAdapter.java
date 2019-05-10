package com.goniyo.notification.email;

import com.goniyo.notification.email.providers.SendGrid;
import com.goniyo.notification.messagegenerator.MessageGenerator;
import com.goniyo.notification.notification.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class EmailAdapter {
    @Autowired
    private NotificationHandler notificationHandler;
    @Autowired
    MessageGenerator messageGenerator;

    // add validation here
    public String sendEmail(EmailDto emailDto) {
        System.out.println("IN FACADE");
        Email email = new Email(); // TODO map SmsDTO()
        String message = messageGenerator.generateMessage("", email);
        notificationHandler.setNotifierHandler(new SendGrid());
        return notificationHandler.sendNotification(email);

    }
}
