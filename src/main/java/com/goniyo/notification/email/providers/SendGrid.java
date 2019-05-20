package com.goniyo.notification.email.providers;

import com.goniyo.notification.email.Email;
import com.goniyo.notification.email.EmailNotifier;
import com.goniyo.notification.notification.NotificationFailedException;
import com.goniyo.notification.notification.NotificationStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SendGrid implements EmailNotifier<Email> {
    @Autowired
    private SendGridConfig sendGridConfig;


    @Override
    public int getTimeout() {
        return 0;
    }

    @Override
    public Mono<NotificationStatusResponse> send(Email notificationMessage) throws NotificationFailedException {
        return null;
    }
}
