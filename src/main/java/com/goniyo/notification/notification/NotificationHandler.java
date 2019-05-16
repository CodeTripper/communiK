package com.goniyo.notification.notification;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.goniyo.notification.notification.Status.*;

@Component
@Slf4j
public class NotificationHandler<T extends NotificationMessage> {
    private List<Notifier<T>> backupNotifiers;

    public void setBackupNotifierHandlers(List<Notifier<T>> backupNotifiers) {
        this.backupNotifiers = backupNotifiers;

    }

    @Autowired
    List<NotificationObserver> notificationObservers;

    private void addObservers(T notificationMessage) {
        notificationObservers.forEach(notificationMessage::addPropertyChangeListener
        );
    }

    public String sendNotification(@NonNull Notifier<T> notifier, @NonNull T notificationMessage) {
        log.info("sending notification" + notificationMessage);
        addObservers(notificationMessage);
        notificationMessage.setStatus(Status.NOTIFICATION_NEW);
        String returnValue = null;
        try {
            returnValue = notifier.send(notificationMessage);
            notificationMessage.setStatus(NOTIFICATION_SENT);
        } catch (NotificationFailedException e) {
            e.printStackTrace();
            notificationMessage.setStatus(NOTIFICATION_FAILED);
        }

        return returnValue;
    }

    private void retryWithBackup(T notificationMessage) {
        // FIXME exit after first success
        backupNotifiers.forEach(notifier -> {
            try {
                notifier.send(notificationMessage);
            } catch (NotificationFailedException e) {
                e.printStackTrace();
                notificationMessage.setStatus(NOTIFICATION_RETRY_FAILED);
            }
        });

    }

    /*public void doSomething(String input) {
        backupNotifiers.stream().filter(c -> c.getName().contains(input)).findFirst().ifPresent(c -> {
            System.out.println(input);
        });
    }*/
}
