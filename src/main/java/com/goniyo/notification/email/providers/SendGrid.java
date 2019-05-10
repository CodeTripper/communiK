package com.goniyo.notification.email.providers;

import com.goniyo.notification.email.Email;
import com.goniyo.notification.email.EmailNotifier;
import com.goniyo.notification.notification.NotificationMessage;


public class SendGrid implements EmailNotifier {
    @Override
    public String send(NotificationMessage notificationMessage) {
        Email email = (Email) notificationMessage;
        System.out.println("Inside SENDGRID Email Notifier" + email.toString());
        return "SENDGRID";
    }
}
