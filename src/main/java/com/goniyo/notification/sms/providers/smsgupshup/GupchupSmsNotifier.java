package com.goniyo.notification.sms.providers.smsgupshup;

import com.goniyo.notification.notification.NotificationFailedException;
import com.goniyo.notification.notification.NotificationStatusResponse;
import com.goniyo.notification.sms.Sms;
import com.goniyo.notification.sms.SmsNotifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Primary
public class GupchupSmsNotifier implements SmsNotifier<Sms> {

    @Override
    public int getTimeout() {
        return 0;
    }

    @Override
    public Mono<NotificationStatusResponse> send(Sms sms) throws NotificationFailedException {
        System.out.println("Inside Gupshup Notifier" + sms.toString());
        // ADD web flux webclient to call Gupchup
        return null;
    }


}
