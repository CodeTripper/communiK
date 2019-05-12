package com.goniyo.notification.sms.providers.twofactor;

import com.goniyo.notification.sms.Sms;
import com.goniyo.notification.sms.SmsNotifier;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

@Service("OTP")
public class TwoFactorSmsNotifier implements SmsNotifier<Sms> {
    @Override
    @HystrixCommand(fallbackMethod = "fallback")
    public String send(Sms sms) {
        System.out.println("Inside Twofactor Notifier" + sms.toString());
        // ADD web flux webclient to call Gupchup
        return "TWOFACTOR";
    }

    public String fallback(Sms sms) {
        System.out.println("Inside Twofactor FALLBACK  Notifier" + sms.toString());
        // ADD web flux webclient to call Gupchup
        return "GUPCHUP";
    }


}
