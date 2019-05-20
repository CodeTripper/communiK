package com.goniyo.notification.notification;

import com.goniyo.notification.repository.NotificationPersistence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.goniyo.notification.notification.Status.NOTIFICATION_FAILED;
import static com.goniyo.notification.notification.Status.NOTIFICATION_SENT;

@Slf4j
@Component
public class NotificationSentEventListener {
    @Autowired
    private NotificationPersistence notificationPersistence;

    @EventListener(condition = "#event.success")
    public void handleSuccessful(NotificationSentEvent<NotificationMessage> event) {
        log.debug("Received successful NotificationSentEvent {}", event);
        NotificationMessage notificationMessage = (NotificationMessage) event.getSource();
        notificationMessage.setStatus(Status.NOTIFICATION_SUCCESS);
        notificationPersistence.update(notificationMessage);
    }

    @EventListener(condition = "#event.success eq false")
    public void handleFailure(NotificationSentEvent<NotificationMessage> event) {
        log.debug("Received failed NotificationSentEvent {}", event);
        NotificationMessage notificationMessage = (NotificationMessage) event.getSource();
        notificationMessage.setStatus(NOTIFICATION_FAILED);
        notificationPersistence.update(notificationMessage);
        Mono<NotificationStatusResponse> returnValue = null;
        NotificationMessage.Notifiers notifiers = notificationMessage.getNotifiers();
        notifiers.getBackup().forEach(notifier -> {
                    try {
                        notifier.send(notificationMessage);
                    } catch (NotificationFailedException e) {
                        e.printStackTrace();
                    }
                }
        );
        notificationMessage.setStatus(NOTIFICATION_SENT);
        log.debug("Notification event sent", event);
    }
}
