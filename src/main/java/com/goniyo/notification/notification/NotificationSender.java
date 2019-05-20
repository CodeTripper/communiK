package com.goniyo.notification.notification;

import com.goniyo.notification.repository.NotificationPersistence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.goniyo.notification.notification.Status.NOTIFICATION_FAILED;
import static com.goniyo.notification.notification.Status.NOTIFICATION_SENT;

@Component
@Slf4j
public class NotificationSender<T extends NotificationMessage> {
    @Autowired
    private NotificationPersistence notificationPersistence;

    public void sendNotification(NotificationMessage notificationMessage) {
        log.info("sending notification" + notificationMessage);
        //notificationMessage.setStatus(Status.NOTIFICATION_NEW);
        Mono<NotificationStatusResponse> returnValue = null;
        try {
            NotificationMessage.Notifiers notifiers = notificationMessage.getNotifiers();
            returnValue = notifiers.getPrimary().send(notificationMessage);
            // TODO If success, create an Notification SUCCESS event, else Notification Failed Event
            notificationMessage.setStatus(NOTIFICATION_SENT);
        } catch (NotificationFailedException e) {
            e.printStackTrace();
            notificationMessage.setStatus(NOTIFICATION_FAILED);
        }
        //return notificationPersistence.store(notificationMessage);/
    }

}
