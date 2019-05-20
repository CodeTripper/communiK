package com.goniyo.notification.notification;

import com.goniyo.notification.repository.NotificationPersistence;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class NotificationHandler<T extends NotificationMessage> {
    @Autowired
    private NotificationPersistence<T> notificationPersistence;
    @Autowired
    private ApplicationEventPublisher publisher;

    public Mono<NotificationMessage> sendNotification(@NonNull T notificationMessage) {
        log.info("About to persist notification {}", notificationMessage);
        notificationMessage.setStatus(Status.NOTIFICATION_NEW);
        // store in DB via async
        // call emailer via asyn
        // update db via async
        Mono<NotificationMessage> notificationMessageMono = notificationPersistence.store(notificationMessage).doOnSuccess(message -> {
            notificationMessage.setId(message.getId());
            this.publisher.publishEvent(new NotificationCreationEvent<>(notificationMessage, true));
            log.debug("Notification persistence successful, NotificationCreationEvent created");
        }).doOnError(message -> {
            this.publisher.publishEvent(new NotificationCreationEvent<>(notificationMessage, false));
            log.debug("Notification persistence unsuccessful, NotificationCreationEvent created");

        });
        return notificationMessageMono;
    }
}
