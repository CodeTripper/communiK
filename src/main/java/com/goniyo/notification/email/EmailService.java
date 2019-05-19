package com.goniyo.notification.email;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.repository.mongo.NotificationMessageDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<NotificationMessage> send(EmailDto emailDto);
    Flux<NotificationMessageDto> getAllEmails();

}
