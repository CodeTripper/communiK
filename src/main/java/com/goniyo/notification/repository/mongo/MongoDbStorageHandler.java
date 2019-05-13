package com.goniyo.notification.repository.mongo;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationStorageResponse;
import com.goniyo.notification.repository.NotificationStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Observable;

@Service
public class MongoDbStorageHandler implements NotificationStorage {
    private static final Logger logger = LoggerFactory.getLogger(MongoDbStorageHandler.class);
    @Override
    public NotificationStorageResponse store(NotificationMessage notificationMessage) {
        // TODO write to mongo
        // TODO add hystrix here
        logger.debug("stored");
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
        logger.debug("updated");
        //notificationMessage.setStatus("UPDATED");
        return new NotificationStorageResponse();
    }

    @Override
    public NotificationStorageResponse status(String id) {
        // TODO add hystrix here
        return null;
    }

    @Override
    public void update(Observable o, Object status) {
        logger.debug("MongoDb: " + status);
        if (status.equals(NotificationMessage.Status.NOTIFICATION_NEW)) {
            this.store((NotificationMessage) o);
        } else {
            this.update((NotificationMessage) o);
        }


    }
}
