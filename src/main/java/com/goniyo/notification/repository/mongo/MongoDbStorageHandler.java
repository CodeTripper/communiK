package com.goniyo.notification.repository.mongo;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationStorageResponse;
import com.goniyo.notification.repository.NotificationStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;

@Service
@Slf4j
public class MongoDbStorageHandler implements NotificationStorage {
    @Override
    public NotificationStorageResponse store(NotificationMessage notificationMessage) {
        // TODO write to mongo
        // TODO add hystrix here
        log.debug("stored");
        notificationMessage.setStatus(NotificationMessage.Status.NOTIFICATION_STORED);
        return new NotificationStorageResponse();
    }

    @PostConstruct
    public void init() {
        System.out.println("repo from @service");
    }

    @Override
    public NotificationStorageResponse update(NotificationMessage notificationMessage) {
        // TODO add hystrix here
        // WARN DO NOT UPDATE STATUS OF notificationMessage HERE
        log.debug("updated");
        //notificationMessage.setStatus("UPDATED");
        return new NotificationStorageResponse();
    }

    @Override
    public NotificationStorageResponse status(String id) {
        // TODO add hystrix here
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        log.debug("MongoDb: " + evt.getOldValue());
        if (evt.getOldValue().equals(NotificationMessage.Status.NOTIFICATION_NEW)) {
            this.store((NotificationMessage) evt.getNewValue());
        } else {
            this.update((NotificationMessage) evt.getNewValue());
        }
    }
}
