package com.goniyo.notification.notification;

import com.goniyo.notification.repository.NotificationPersistence;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@Slf4j
public class NotificationHandler<T extends NotificationMessage> {
    @Autowired
    private NotificationPersistence<T> notificationPersistence;

    public Mono<NotificationStatusResponse> sendNotification(@NonNull T notificationMessage) {
        log.info("About to persist notification {}", notificationMessage);
        notificationMessage.setStatus(Status.NOTIFICATION_NEW);
        return notificationPersistence.store(notificationMessage)
                //.timeout(Duration.ofMillis(1800))
                .map(message -> {
                    notificationMessage.setStatus(Status.NOTIFICATION_STORED);
                    notificationMessage.setId(message.getId());
                    return notificationMessage;
                })
                .flatMap(message -> notificationMessage.getNotifiers().getPrimary().send(notificationMessage))
                // .timeout(Duration.ofMillis(4800))
                .onErrorReturn(getFailure(notificationMessage));

    }

    public Mono<NotificationStatusResponse> getFailure(@NonNull T notificationMessage) {
        NotificationStatusResponse notificationStatusResponse = new NotificationStatusResponse();
        notificationStatusResponse.setRequestId(notificationMessage.getId());
        notificationStatusResponse.setStatus(false);
        notificationStatusResponse.setResponseReceivedAt(LocalDateTime.now());
        return Mono.just(notificationStatusResponse);
    }

}
