package com.goniyo.notification.sms.providers.twofactor;

import com.goniyo.notification.sms.Sms;
import com.goniyo.notification.sms.SmsNotifier;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorSmsNotifier implements SmsNotifier<Sms> {
    @Override
    public String send(Sms sms) {
        System.out.println("Inside Twofactor Notifier" + sms.toString());
        // ADD web flux webclient to call Gupchup
        return "GUPCHUP";
    }


}
