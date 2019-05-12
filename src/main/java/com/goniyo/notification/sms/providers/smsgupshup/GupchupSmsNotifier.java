package com.goniyo.notification.sms.providers.smsgupshup;

import com.goniyo.notification.sms.Sms;
import com.goniyo.notification.sms.SmsNotifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class GupchupSmsNotifier implements SmsNotifier<Sms> {

    @Override
    public String send(Sms sms) {
        System.out.println("Inside Gupshup Notifier" + sms.toString());
        // ADD web flux webclient to call Gupchup
        return "GUPCHUP";
    }


}
