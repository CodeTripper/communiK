package com.goniyo.notification.email;

import com.goniyo.notification.notification.NotificationStatusResponse;
import com.goniyo.notification.repository.mongo.NotificationMessageDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<NotificationStatusResponse> send(EmailDto emailDto);
    Flux<NotificationMessageDto> getAllEmails();

}
