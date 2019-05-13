package com.goniyo.notification.repository.mongo;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationStorageResponse;
import com.goniyo.notification.repository.NotificationStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Observable;
import java.util.Observer;

@Service
public class MongoDbStorageHandler implements NotificationStorage, Observer {
    private static final Logger logger = LoggerFactory.getLogger(MongoDbStorageHandler.class);
    @Override
    public NotificationStorageResponse store(NotificationMessage notificationMessage) {
        // TODO write to mongo
        // TODO add hystrix here
        logger.debug("stored");
        notificationMessage.setStatus("STORED");
        return new NotificationStorageResponse();
    }

    @PostConstruct
    public void init() {
        System.out.println("repo from @service");
    }

    @Override
    public NotificationStorageResponse update(NotificationMessage notificationMessage) {
        // TODO add hystrix here
        logger.debug("updated");
        return new NotificationStorageResponse();
    }

    @Override
    public NotificationStorageResponse status(String id) {
        // TODO add hystrix here
        return null;
    }

    @Override
    public void update(Observable o, Object status) {
        System.out.println("MongoDb: " + status);
        if (status.equals("NEW")) {
            store((NotificationMessage) o);
        }
    }
}
