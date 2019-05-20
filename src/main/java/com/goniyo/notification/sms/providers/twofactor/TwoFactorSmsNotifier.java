package com.goniyo.notification.sms.providers.twofactor;

import com.goniyo.notification.notification.NotificationFailedException;
import com.goniyo.notification.notification.NotificationStatusResponse;
import com.goniyo.notification.sms.Sms;
import com.goniyo.notification.sms.SmsNotifier;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("OTP")
public class TwoFactorSmsNotifier implements SmsNotifier<Sms> {
    @Override
    public int getTimeout() {
        return 0;
    }

    @Override
    @HystrixCommand(fallbackMethod = "fallback")
    public Mono<NotificationStatusResponse> send(Sms sms) throws NotificationFailedException {
        System.out.println("Inside Twofactor Notifier" + sms.toString());
        // ADD web flux webclient to call Gupchup
        return null;
    }

    public String fallback(Sms sms) {
        System.out.println("Inside Twofactor FALLBACK  Notifier" + sms.toString());
        // ADD web flux webclient to call Gupchup
        return "GUPCHUP";
    }


}
