package com.goniyo.notification.sms.providers.smsgupshup;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.sms.Sms;
import com.goniyo.notification.sms.SmsNotifier;

public class GupchupSmsNotifer implements SmsNotifier {

    @Override
    public String send(NotificationMessage notificationMessage) {
        Sms sms = (Sms) notificationMessage;
        System.out.println("Inside Gupshup Notifier" + sms.toString());
        // ADD web flux webclient to call Gupchup
        return "GUPCHUP";
    }
}
