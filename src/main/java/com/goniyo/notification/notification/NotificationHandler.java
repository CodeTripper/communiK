package com.goniyo.notification.notification;

import com.goniyo.notification.repository.NotificationStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationHandler {

    @Autowired
    NotificationStorage notificationStorage;

    Notifier notifier;

    public void setNotifierHandler(Notifier notifier) {
        this.notifier = notifier;
    }


    public String sendNotification(NotificationMessage notificationMessage) {
        System.out.println("NOTIFIED");
        // TODO handler nullpointer if NotificationStorage is null
        // TODO use a pattern here
        // 1. call store
        // 3. call retry
        // 4. add max retry
        // get list of backups providers
        // get primary provider'
        // what about failed ones?
        // send response or async via mongo
        // update DB
        notificationStorage.store(notificationMessage);

        String returnValue = notifier.send(notificationMessage);
        notificationStorage.update(notificationMessage);

        return returnValue;
    }

    public String getNotificationStatus(String id) {
        NotificationStorageResponse notificationStorageResponse = notificationStorage.status(id);
        return "SUCCESS";
    }
}
