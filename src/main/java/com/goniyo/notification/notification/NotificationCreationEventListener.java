package com.goniyo.notification.notification;

import com.goniyo.notification.repository.NotificationPersistence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.goniyo.notification.notification.Status.NOTIFICATION_SENT;

@Slf4j
@Component
public class NotificationCreationEventListener {
    @Autowired
    private NotificationPersistence notificationPersistence;
    @Autowired
    private ApplicationEventPublisher publisher;

    @EventListener(condition = "#event.success")
    public void handleSuccessful(NotificationCreationEvent<NotificationMessage> event) {
        log.debug("Received successful NotificationCreationEvent {}", event);
        NotificationMessage notificationMessage = (NotificationMessage) event.getSource();
        notificationMessage.setStatus(NOTIFICATION_SENT);
            NotificationMessage.Notifiers notifiers = notificationMessage.getNotifiers();
            notifiers.getPrimary().send(notificationMessage).single().subscribe(message -> {
                notificationMessage.setStatus(Status.NOTIFICATION_SUCCESS);
                this.publisher.publishEvent(new NotificationSentEvent<>(notificationMessage, true));
                log.debug("Notification sent successful, NotificationSentEvent created");

            });


    }

    @EventListener(condition = "#event.success eq false")
    public void handleFailure(NotificationCreationEvent<NotificationMessage> event) {
        log.debug("Received unsuccessful NotificationCreationEvent {}", event);
    }


}
