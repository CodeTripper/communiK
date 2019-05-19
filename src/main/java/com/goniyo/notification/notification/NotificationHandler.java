package com.goniyo.notification.notification;

import com.goniyo.notification.repository.NotificationPersistence;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class NotificationHandler<T extends NotificationMessage> {
    @Autowired
    private NotificationPersistence notificationPersistence;

    @Autowired
    private List<NotificationObserver> notificationObservers;

    // TODO
    // should I store attachment?
    // How to forward to the correct notifier?
    // How to get the backup notifier?
    // The flow to be finalized
    public Mono<NotificationMessage> sendNotification(@NonNull Notifier<T> notifier, @NonNull T notificationMessage) {
        log.info("sending notification" + notificationMessage);
        //addObservers(notificationMessage);
        notificationMessage.setStatus(Status.NOTIFICATION_NEW);
        return notificationPersistence.store(notificationMessage);
        /*String returnValue = null;
        try {
            returnValue = notifier.send(notificationMessage);
            notificationMessage.setStatus(NOTIFICATION_SENT);
        } catch (NotificationFailedException e) {
            e.printStackTrace();
            notificationMessage.setStatus(NOTIFICATION_FAILED);
        }
*/
        //return returnValue;
    }

    /*public void doSomething(String input) {
        backupNotifiers.stream().filter(c -> c.getName().contains(input)).findFirst().ifPresent(c -> {
            System.out.println(input);
        });
    }*/
}
