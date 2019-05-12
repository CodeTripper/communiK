package com.goniyo.notification.email.providers;

import com.goniyo.notification.email.Email;
import com.goniyo.notification.email.EmailNotifier;
import org.springframework.stereotype.Service;

@Service
public class SendGrid implements EmailNotifier<Email> {
    @Override
    public String send(Email email) {
        System.out.println("Inside SENDGRID Email Notifier" + email.toString());
        return "SENDGRID";
    }


}
