package com.goniyo.notification.sms;

import com.goniyo.notification.messagegenerator.MessageGenerator;
import com.goniyo.notification.notification.NotificationHandler;
import com.goniyo.notification.notification.NotificationResponse;
import com.goniyo.notification.sms.providers.smsgupshup.GupchupSmsNotifer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO should be a port/interface
@Component
class SmsAdapter implements SmsService {
    @Autowired
    NotificationHandler notificationHandler;
    @Autowired
    MessageGenerator messageGenerator;

    // add validation here
    public String sendSms(SmsDTO smsDTO) {
        // TODO add sl4j
        System.out.println("IN FACADE");

        Sms sms = new Sms(); // map SmsDTO()
        String message = messageGenerator.generateMessage("", sms);
        notificationHandler.setNotifierHandler(new GupchupSmsNotifer());
        return notificationHandler.sendNotification(sms);

    }

    @Override
    public NotificationResponse getSmsStatus(String id) {
        notificationHandler.getNotificationStatus(id);
        return null;
    }

}
